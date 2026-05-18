package com.raithavarta.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raithavarta.app.R
import com.raithavarta.app.data.Tip

class TipAdapter(
    private var items: List<Tip>,
    private val lang: String,
    private val onClick: (Tip) -> Unit
) : RecyclerView.Adapter<TipAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val emoji: TextView = view.findViewById(R.id.tipEmoji)
        val crop: TextView = view.findViewById(R.id.tipCropBadge)
        val title: TextView = view.findViewById(R.id.tipTitle)
        val meta: TextView = view.findViewById(R.id.tipMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_tip, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Tip>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val t = items[position]
        holder.emoji.text = t.emoji
        holder.crop.text = if (lang == "kn") t.cropKn else t.cropEn
        holder.title.text = if (lang == "kn") t.actionKn else t.actionEn
        holder.meta.text = if (lang == "kn") t.timeAgoKn else t.timeAgoEn
        holder.itemView.setOnClickListener { onClick(t) }
    }
}
