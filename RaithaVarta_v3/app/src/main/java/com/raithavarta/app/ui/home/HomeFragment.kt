package com.raithavarta.app.ui.home

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.raithavarta.app.R
import com.raithavarta.app.data.DataRepository
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.data.Tip
import com.raithavarta.app.databinding.FragmentHomeBinding
import com.raithavarta.app.ui.adapters.FlashCardBinder
import com.raithavarta.app.ui.tips.TipsFragment
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val PREFS = "raitha_prefs"
    private val KEY_SAVED_TIP_IDS = "saved_tip_ids"

    private var currentIndex = 0
    private var selectedCat = 0

    private val catCropKeys = listOf("all", "paddy", "tomato", "coconut", "banana", "sugarcane")

    private val filteredTips: List<Tip>
        get() {
            val key = catCropKeys.getOrElse(selectedCat) { "all" }
            return if (key == "all") DataRepository.tips
            else DataRepository.tips.filter { it.cropEn.lowercase().contains(key) }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val district = prefs.getString("user_district", null)
        if (!district.isNullOrBlank()) binding.weatherBadge.text = "☀️ 28°C  $district"

        renderCategoryPills()
        renderStories()

        binding.swipeSkip.setOnClickListener { onSwipeLeft() }
        binding.swipeSave.setOnClickListener { onSwipeRight() }

        // "See all" → navigate to Tips tab showing all stories
        binding.seeAll.setOnClickListener {
            (requireActivity() as? com.raithavarta.app.MainActivity)?.navigateTo(
                TipsFragment(), R.id.nav_tips
            )
        }

        binding.uploadLeafBtn.setOnClickListener {
            (requireActivity() as? com.raithavarta.app.MainActivity)?.navigateTo(
                com.raithavarta.app.ui.diagnose.DiagnoseFragment(), R.id.nav_diagnose
            )
        }

        renderCard()
        attachSwipe()
        renderDots()
    }

    private fun renderCategoryPills() {
        val cats = resources.getStringArray(R.array.cats).toList()
        binding.catsRow.removeAllViews()
        cats.forEachIndexed { i, label ->
            val pill = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_category_pill, binding.catsRow, false) as TextView
            pill.text = label
            pill.setBackgroundResource(
                if (i == selectedCat) R.drawable.bg_pill_active else R.drawable.bg_pill_inactive
            )
            pill.setOnClickListener {
                selectedCat = i
                currentIndex = 0
                renderCategoryPills()
                renderCard()
                renderDots()
            }
            binding.catsRow.addView(pill)
        }
    }

    private fun renderStories() {
        val lang = LanguageManager.getCurrentLang(requireContext())
        binding.storiesContainer.removeAllViews()
        for (s in DataRepository.stories) {
            val row = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_story, binding.storiesContainer, false)
            row.findViewById<TextView>(R.id.storyAvatar).text = s.avatar
            row.findViewById<TextView>(R.id.storyName).text = if (lang == "kn") s.nameKn else s.nameEn
            row.findViewById<TextView>(R.id.storyLoc).text = if (lang == "kn") s.locKn else s.locEn
            row.findViewById<TextView>(R.id.storyText).text = if (lang == "kn") s.textKn else s.textEn
            row.findViewById<TextView>(R.id.storyResult).text = if (lang == "kn") s.resultKn else s.resultEn
            binding.storiesContainer.addView(row)
        }
    }

    private fun renderCard() {
        binding.cardStack.removeAllViews()
        val tips = filteredTips
        if (tips.isEmpty() || currentIndex >= tips.size) {
            val empty = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
            empty.addView(TextView(requireContext()).apply {
                text = if (tips.isEmpty()) getString(R.string.no_tips) else getString(R.string.all_done)
                textSize = 22f; gravity = Gravity.CENTER
            })
            empty.addView(TextView(requireContext()).apply {
                text = getString(R.string.all_done_sub); textSize = 13f; gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_muted))
                setPadding(0, 8, 0, 0)
            })
            binding.cardStack.addView(empty)
            renderDots(); return
        }
        val tip = tips[currentIndex]
        val cardView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_flash_card, binding.cardStack, false)
        FlashCardBinder.bind(cardView, tip, LanguageManager.getCurrentLang(requireContext()))
        binding.cardStack.addView(cardView)
        renderDots()
    }

    private fun renderDots() {
        binding.cardDots.removeAllViews()
        for (i in filteredTips.indices) {
            val dot = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_dot, binding.cardDots, false)
            dot.setBackgroundResource(if (i == currentIndex) R.drawable.bg_dot_active else R.drawable.bg_dot)
            binding.cardDots.addView(dot)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachSwipe() {
        val touchSlop = ViewConfiguration.get(requireContext()).scaledTouchSlop
        val swipeThreshold = (resources.displayMetrics.density * 60).toInt()
        var startX = 0f; var startY = 0f; var tracking = false

        binding.cardStack.setOnTouchListener { v, ev ->
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = ev.rawX; startY = ev.rawY; tracking = false
                    v.parent?.requestDisallowInterceptTouchEvent(true); true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = ev.rawX - startX; val dy = ev.rawY - startY
                    if (!tracking && (abs(dx) > touchSlop || abs(dy) > touchSlop)) {
                        if (abs(dx) > abs(dy)) { tracking = true; v.parent?.requestDisallowInterceptTouchEvent(true) }
                        else v.parent?.requestDisallowInterceptTouchEvent(false)
                    }
                    if (tracking) {
                        val card = if (binding.cardStack.childCount > 0) binding.cardStack.getChildAt(0) else null
                        card?.translationX = dx; card?.rotation = (dx / 30f).coerceIn(-15f, 15f)
                    }; true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val dx = ev.rawX - startX
                    val card = if (binding.cardStack.childCount > 0) binding.cardStack.getChildAt(0) else null
                    if (tracking && abs(dx) > swipeThreshold) { if (dx > 0) onSwipeRight() else onSwipeLeft() }
                    else card?.animate()?.translationX(0f)?.rotation(0f)?.setDuration(180)?.start()
                    v.parent?.requestDisallowInterceptTouchEvent(false); tracking = false; true
                }
                else -> false
            }
        }
    }

    private fun onSwipeRight() {
        // Persist this tip's id to SharedPrefs so TipsFragment "Saved" tab can show it
        val tips = filteredTips
        if (currentIndex < tips.size) {
            val tipId = tips[currentIndex].id
            val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val savedIds = prefs.getStringSet(KEY_SAVED_TIP_IDS, mutableSetOf())!!.toMutableSet()
            savedIds.add(tipId.toString())
            prefs.edit().putStringSet(KEY_SAVED_TIP_IDS, savedIds).apply()

            // Update Saved stat count on profile
            prefs.edit().putInt("stat_saved_count", savedIds.size).apply()
        }
        Toast.makeText(requireContext(), R.string.saved_toast, Toast.LENGTH_SHORT).show()
        flyOutAndAdvance(toRight = true)
    }

    private fun onSwipeLeft() { flyOutAndAdvance(toRight = false) }

    private fun flyOutAndAdvance(toRight: Boolean) {
        val card = if (binding.cardStack.childCount > 0) binding.cardStack.getChildAt(0) else null
        if (card != null) {
            card.animate().translationX(resources.displayMetrics.widthPixels.toFloat() * if (toRight) 1f else -1f)
                .rotation(if (toRight) 25f else -25f).alpha(0f).setDuration(220).start()
            ObjectAnimator.ofFloat(card, "alpha", 1f, 0f).setDuration(180).start()
        }
        currentIndex++
        binding.cardStack.postDelayed({ renderCard() }, 240)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
