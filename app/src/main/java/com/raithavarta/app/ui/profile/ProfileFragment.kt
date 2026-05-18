package com.raithavarta.app.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.raithavarta.app.LoginActivity
import com.raithavarta.app.R
import com.raithavarta.app.data.DataRepository
import com.raithavarta.app.data.FirebaseManager
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.data.SettingItem
import com.raithavarta.app.databinding.FragmentProfileBinding
import com.raithavarta.app.databinding.ItemSettingBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    companion object {
        private const val PREFS = "raitha_prefs"
        private const val KEY_NAME = "user_name"
        private const val KEY_DISTRICT = "user_district"
        private const val KEY_SELECTED_CROPS = "selected_crops"
        private const val KEY_NOTIF_ENABLED = "notif_enabled"
        private const val KEY_LOGGED_IN = "logged_in"
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // All 31 districts of Karnataka
    private val karnatakaDistricts = arrayOf(
        "Bagalkot", "Ballari (Bellary)", "Belagavi (Belgaum)", "Bengaluru Rural",
        "Bengaluru Urban", "Bidar", "Chamarajanagar", "Chikkaballapura",
        "Chikkamagaluru", "Chitradurga", "Dakshina Kannada", "Davanagere",
        "Dharwad", "Gadag", "Hassan", "Haveri", "Kalaburagi (Gulbarga)",
        "Kodagu (Coorg)", "Kolar", "Koppal", "Mandya", "Mysuru (Mysore)",
        "Raichur", "Ramanagara", "Shivamogga (Shimoga)", "Tumakuru (Tumkur)",
        "Udupi", "Uttara Kannada", "Vijayapura (Bijapur)", "Yadgir",
        "Hubli-Dharwad"
    ).also { it.sort() }

    // Kannada names for each district
    private val districtKannadaMap = mapOf(
        "Bagalkot" to "ಬಾಗಲಕೋಟೆ",
        "Ballari (Bellary)" to "ಬಳ್ಳಾರಿ",
        "Belagavi (Belgaum)" to "ಬೆಳಗಾವಿ",
        "Bengaluru Rural" to "ಬೆಂಗಳೂರು ಗ್ರಾಮಾಂತರ",
        "Bengaluru Urban" to "ಬೆಂಗಳೂರು ನಗರ",
        "Bidar" to "ಬೀದರ್",
        "Chamarajanagar" to "ಚಾಮರಾಜನಗರ",
        "Chikkaballapura" to "ಚಿಕ್ಕಬಳ್ಳಾಪುರ",
        "Chikkamagaluru" to "ಚಿಕ್ಕಮಗಳೂರು",
        "Chitradurga" to "ಚಿತ್ರದುರ್ಗ",
        "Dakshina Kannada" to "ದಕ್ಷಿಣ ಕನ್ನಡ",
        "Davanagere" to "ದಾವಣಗೆರೆ",
        "Dharwad" to "ಧಾರವಾಡ",
        "Gadag" to "ಗದಗ",
        "Hassan" to "ಹಾಸನ",
        "Haveri" to "ಹಾವೇರಿ",
        "Kalaburagi (Gulbarga)" to "ಕಲಬುರಗಿ",
        "Kodagu (Coorg)" to "ಕೊಡಗು",
        "Kolar" to "ಕೋಲಾರ",
        "Koppal" to "ಕೊಪ್ಪಳ",
        "Mandya" to "ಮಂಡ್ಯ",
        "Mysuru (Mysore)" to "ಮೈಸೂರು",
        "Raichur" to "ರಾಯಚೂರು",
        "Ramanagara" to "ರಾಮನಗರ",
        "Shivamogga (Shimoga)" to "ಶಿವಮೊಗ್ಗ",
        "Tumakuru (Tumkur)" to "ತುಮಕೂರು",
        "Udupi" to "ಉಡುಪಿ",
        "Uttara Kannada" to "ಉತ್ತರ ಕನ್ನಡ",
        "Vijayapura (Bijapur)" to "ವಿಜಯಪುರ",
        "Yadgir" to "ಯಾದಗಿರಿ",
        "Hubli-Dharwad" to "ಹುಬ್ಬಳ್ಳಿ-ಧಾರವಾಡ"
    )

    private fun districtDisplay(english: String): String {
        val lang = LanguageManager.getCurrentLang(requireContext())
        return if (lang == "kn") districtKannadaMap[english] ?: english else english
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ── Load real user name & district from SharedPrefs ──
        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val savedName = prefs.getString(KEY_NAME, null)
        val savedDistrict = prefs.getString(KEY_DISTRICT, null)

        if (!savedName.isNullOrBlank()) {
            binding.profileName.text = savedName
        }
        if (!savedDistrict.isNullOrBlank()) {
            binding.profileLoc.text = getString(R.string.location_format, districtDisplay(savedDistrict))
        }

        renderLangToggle()
        binding.langEn.setOnClickListener { setLang("en") }
        binding.langKn.setOnClickListener { setLang("kn") }

        // ── Logout button ──
        binding.btnLogout.setOnClickListener { confirmLogout() }

        binding.settingsList.removeAllViews()
        for (item in DataRepository.settings) {
            val rowBinding = ItemSettingBinding.inflate(layoutInflater, binding.settingsList, false)
            rowBinding.settingIcon.text = item.icon
            rowBinding.settingLabel.setText(item.labelRes)
            when (item.id) {
                "district" -> rowBinding.settingSub.text = savedDistrict?.let { getString(R.string.district_format, districtDisplay(it)) }
                    ?: getString(R.string.set_district_sub)
                "crops"    -> rowBinding.settingSub.text = getSavedCropsDisplay()
                "notif"    -> rowBinding.settingSub.text = getNotifDisplay()
                else       -> rowBinding.settingSub.setText(item.subRes)
            }
            rowBinding.root.setOnClickListener { openSettingDialog(item, rowBinding) }
            binding.settingsList.addView(rowBinding.root)
        }
    }

    private fun getSavedCropsDisplay(): String {
        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val saved = prefs.getString(KEY_SELECTED_CROPS, null)
        return if (saved.isNullOrBlank()) getString(R.string.set_crops_sub) else saved
    }

    private fun getNotifDisplay(): String {
        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean(KEY_NOTIF_ENABLED, true)
        return if (enabled) getString(R.string.set_notif_sub) else getString(R.string.notif_disabled)
    }

    private fun renderLangToggle() {
        val current = LanguageManager.getCurrentLang(requireContext())
        if (current == "kn") {
            binding.langEn.setBackgroundResource(R.drawable.bg_lang_inactive)
            binding.langKn.setBackgroundResource(R.drawable.bg_lang_active)
        } else {
            binding.langEn.setBackgroundResource(R.drawable.bg_lang_active)
            binding.langKn.setBackgroundResource(R.drawable.bg_lang_inactive)
        }
    }

    private fun setLang(lang: String) {
        if (LanguageManager.getCurrentLang(requireContext()) == lang) return
        LanguageManager.setLanguage(requireContext(), lang)
        requireActivity().recreate()
    }

    private fun openSettingDialog(item: SettingItem, rowBinding: ItemSettingBinding) {
        when (item.id) {
            "district" -> showDistrictDialog(rowBinding)
            "crops"    -> showCropsDialog(rowBinding)
            "notif"    -> showNotifDialog(rowBinding)
            "kvk"      -> showKvkDialog()
            else       -> showSimpleDialog(item)
        }
    }

    /** Single-choice list of all 31 Karnataka districts */
    private fun showDistrictDialog(rowBinding: ItemSettingBinding) {
        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val current = prefs.getString(KEY_DISTRICT, null)
        val currentIdx = karnatakaDistricts.indexOfFirst {
            it.equals(current, ignoreCase = true) ||
            it.startsWith(current ?: "", ignoreCase = true)
        }.coerceAtLeast(0)

        // Show Kannada names in dialog when Kannada is selected
        val lang = LanguageManager.getCurrentLang(requireContext())
        val displayDistricts = if (lang == "kn") {
            Array(karnatakaDistricts.size) { i -> districtKannadaMap[karnatakaDistricts[i]] ?: karnatakaDistricts[i] }
        } else {
            karnatakaDistricts
        }

        var chosen = currentIdx

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.set_district)
            .setSingleChoiceItems(displayDistricts, currentIdx) { _, which ->
                chosen = which
            }
            .setPositiveButton(R.string.dialog_save) { _, _ ->
                val district = karnatakaDistricts[chosen]
                prefs.edit { putString(KEY_DISTRICT, district) }
                rowBinding.settingSub.text = getString(R.string.district_format, districtDisplay(district))
                // Update profile header live
                binding.profileLoc.text = getString(R.string.location_format, districtDisplay(district))
                // Sync to Firestore (best-effort)
                lifecycleScope.launch {
                    try { FirebaseManager.updateUserField("district", district) } catch (_: Exception) {}
                }
            }
            .setNegativeButton(R.string.dialog_close, null)
            .show()
    }

    /** Multi-select checklist of all available crops */
    private fun showCropsDialog(rowBinding: ItemSettingBinding) {
        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val savedSet = prefs.getString(KEY_SELECTED_CROPS, "Tomato, Paddy, Coconut")
            ?.split(",")?.map { it.trim() }?.toMutableSet() ?: mutableSetOf()

        val allCrops = DataRepository.cropOptions.map { it.nameEn }
        val lang = LanguageManager.getCurrentLang(requireContext())
        val displayCrops = if (lang == "kn") {
            DataRepository.cropOptions.map { it.nameKn }
        } else {
            allCrops
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 16, 48, 8)
        }

        val checkBoxes = displayCrops.mapIndexed { idx, cropLabel ->
            CheckBox(requireContext()).apply {
                text = cropLabel
                isChecked = savedSet.contains(allCrops[idx])
                textSize = 16f
                setPadding(0, 12, 0, 12)
            }.also { container.addView(it) }
        }

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.set_crops)
            .setView(container)
            .setPositiveButton(R.string.dialog_save) { _, _ ->
                val selected = allCrops.filterIndexed { i, _ -> checkBoxes[i].isChecked }
                val display = selected.joinToString(", ").ifBlank { "None" }
                prefs.edit { putString(KEY_SELECTED_CROPS, display) }
                // Show Kannada crop names in subtitle too
                val displaySelected = if (lang == "kn") {
                    DataRepository.cropOptions.filterIndexed { i, _ -> checkBoxes[i].isChecked }
                        .joinToString(", ") { it.nameKn }.ifBlank { "ಯಾವುದೂ ಇಲ್ಲ" }
                } else display
                rowBinding.settingSub.text = displaySelected
                // Sync to Firestore (best-effort)
                lifecycleScope.launch {
                    try { FirebaseManager.updateUserField("crops", display) } catch (_: Exception) {}
                }
            }
            .setNegativeButton(R.string.dialog_close, null)
            .show()
    }

    /** Notification dialog with a toggle switch */
    private fun showNotifDialog(rowBinding: ItemSettingBinding) {
        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean(KEY_NOTIF_ENABLED, true)

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }
        val label = TextView(requireContext()).apply {
            text = getString(R.string.dialog_notif_msg)
            textSize = 14f
        }
        val row = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
            setPadding(0, 24, 0, 8)
        }
        val switchLabel = TextView(requireContext()).apply {
            text = getString(R.string.notif_toggle_label)
            textSize = 15f
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        val toggle = SwitchCompat(requireContext()).apply { isChecked = isEnabled }

        row.addView(switchLabel)
        row.addView(toggle)
        container.addView(label)
        container.addView(row)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.set_notif)
            .setView(container)
            .setPositiveButton(R.string.dialog_save) { _, _ ->
                prefs.edit { putBoolean(KEY_NOTIF_ENABLED, toggle.isChecked) }
                rowBinding.settingSub.text = getNotifDisplay()
            }
            .setNegativeButton(R.string.dialog_close, null)
            .show()
    }

    private fun showKvkDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.set_kvk)
            .setMessage(R.string.dialog_kvk_msg)
            .setPositiveButton(R.string.dialog_call) { _, _ ->
                startActivity(Intent(Intent.ACTION_DIAL,
                    getString(R.string.kvk_phone_number).let { "tel:$it".toUri() }))
            }
            .setNegativeButton(R.string.dialog_close, null)
            .show()
    }

    private fun showSimpleDialog(item: SettingItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(item.dialogTitleRes)
            .setMessage(item.dialogMsgRes)
            .setNegativeButton(R.string.dialog_close, null)
            .show()
    }

    private fun confirmLogout() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.logout_confirm) { _, _ -> doLogout() }
            .setNegativeButton(R.string.dialog_close, null)
            .show()
    }

    private fun doLogout() {
        requireContext()
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit { putBoolean(KEY_LOGGED_IN, false) }

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
