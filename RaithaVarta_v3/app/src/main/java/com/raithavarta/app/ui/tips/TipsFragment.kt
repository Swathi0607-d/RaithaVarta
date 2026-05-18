package com.raithavarta.app.ui.tips

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.raithavarta.app.R
import com.raithavarta.app.data.DataRepository
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.data.Tip
import com.raithavarta.app.databinding.FragmentTipsBinding
import com.raithavarta.app.ui.adapters.CategoryAdapter
import com.raithavarta.app.ui.adapters.TipAdapter

class TipsFragment : Fragment() {

    private var _binding: FragmentTipsBinding? = null
    private val binding get() = _binding!!

    private val PREFS = "raitha_prefs"
    private val KEY_SAVED_TIP_IDS = "saved_tip_ids"

    // 0 = All Tips, 1 = Saved Tips
    private var activeTab = 0
    private lateinit var tipAdapter: TipAdapter
    private lateinit var lang: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lang = LanguageManager.getCurrentLang(requireContext())

        // ── Initialise adapter FIRST before anything calls selectTab ──
        tipAdapter = TipAdapter(DataRepository.tips, lang) { tip -> showTipDialog(tip) }
        binding.tipsList.layoutManager = LinearLayoutManager(requireContext())
        binding.tipsList.adapter = tipAdapter

        // ── Tab buttons (All Tips / Saved Tips) ──
        binding.tabAll.setOnClickListener { selectTab(0) }
        binding.tabSaved.setOnClickListener { selectTab(1) }
        selectTab(0)   // default: All Tips — safe now because tipAdapter is ready

        // ── Crop filter pills ──
        val cats = resources.getStringArray(R.array.cats).toList()
        binding.tipsFilter.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.tipsFilter.adapter = CategoryAdapter(cats) { idx ->
            if (_binding == null) return@CategoryAdapter
            val base = getBaseTips()
            val filtered = if (idx == 0) base else {
                val key = cats[idx].split(" ")[0].lowercase()
                base.filter { it.cropEn.lowercase().contains(key) }
            }
            tipAdapter.setItems(filtered)
        }
    }

    private fun selectTab(tab: Int) {
        activeTab = tab
        // Visual state
        binding.tabAll.setBackgroundResource(
            if (tab == 0) R.drawable.bg_lang_active else R.drawable.bg_lang_inactive
        )
        binding.tabSaved.setBackgroundResource(
            if (tab == 1) R.drawable.bg_lang_active else R.drawable.bg_lang_inactive
        )
        tipAdapter.setItems(getBaseTips())

        // Show empty state label
        binding.emptyLabel.visibility =
            if (getBaseTips().isEmpty()) View.VISIBLE else View.GONE
    }

    private fun getBaseTips(): List<Tip> {
        return if (activeTab == 0) {
            DataRepository.tips
        } else {
            val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val savedIds = prefs.getStringSet(KEY_SAVED_TIP_IDS, emptySet()) ?: emptySet()
            DataRepository.tips.filter { it.id.toString() in savedIds }
        }
    }

    private fun showTipDialog(tip: Tip) {
        val title = if (lang == "kn") tip.actionKn else tip.actionEn
        val msg = if (lang == "kn") tip.detailKn else tip.detailEn
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(R.string.dialog_close, null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
