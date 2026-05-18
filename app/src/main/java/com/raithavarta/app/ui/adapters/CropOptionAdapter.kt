package com.raithavarta.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raithavarta.app.R
import com.raithavarta.app.data.CropOption

class CropOptionAdapter(
    private val items: List<CropOption>,
    private val lang: String,
    private val onSelect: (CropOption) -> Unit
) : RecyclerView.Adapter<CropOptionAdapter.VH>() {

    private var selected = -1

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = view.findViewById(R.id.cropRoot)
        val emoji: TextView = view.findViewById(R.id.cropEmoji)
        val name: TextView = view.findViewById(R.id.cropName)
        val hint: TextView = view.findViewById(R.id.cropHint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_crop_option, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.emoji.text = item.emoji
        holder.name.text = if (lang == "kn") item.nameKn else item.nameEn
        holder.hint.text = if (lang == "kn") item.hintKn else item.hintEn
        holder.root.setBackgroundResource(
            if (position == selected) R.drawable.bg_diag_card_selected else R.drawable.bg_diag_card
        )
        holder.root.setOnClickListener {
            val old = selected
            selected = holder.bindingAdapterPosition
            if (old >= 0) notifyItemChanged(old)
            notifyItemChanged(selected)
            onSelect(item)
        }
    }
}
