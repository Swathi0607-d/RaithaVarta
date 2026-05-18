package com.raithavarta.app.ui.adapters

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import com.raithavarta.app.R
import com.raithavarta.app.data.Tip

object FlashCardBinder {
    fun bind(view: View, tip: Tip, lang: String) {
        val emoji = view.findViewById<TextView>(R.id.fcEmoji)
        val crop = view.findViewById<TextView>(R.id.fcCrop)
        val action = view.findViewById<TextView>(R.id.fcAction)
        val detail = view.findViewById<TextView>(R.id.fcDetail)
        val tag = view.findViewById<TextView>(R.id.fcTag)
        val bg = view.findViewById<View>(R.id.fcBg)

        emoji.text = tip.emoji
        crop.text = if (lang == "kn") tip.cropKn else tip.cropEn
        action.text = if (lang == "kn") tip.actionKn else tip.actionEn
        detail.text = if (lang == "kn") tip.detailKn else tip.detailEn
        tag.text = if (lang == "kn") tip.tagKn else tip.tagEn
        bg.background = ColorDrawable(tip.bgColor)
    }
}
