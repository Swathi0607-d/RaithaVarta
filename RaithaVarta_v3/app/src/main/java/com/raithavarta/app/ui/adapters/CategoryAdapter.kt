package com.raithavarta.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raithavarta.app.R

class CategoryAdapter(
    private val items: List<String>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    private var selected = 0

    inner class VH(view: TextView) : RecyclerView.ViewHolder(view) {
        val text: TextView = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_pill, parent, false) as TextView
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.text.text = items[position]
        holder.text.setBackgroundResource(
            if (position == selected) R.drawable.bg_pill_active else R.drawable.bg_pill_inactive
        )
        holder.text.setOnClickListener {
            val old = selected
            selected = holder.bindingAdapterPosition
            notifyItemChanged(old)
            notifyItemChanged(selected)
            onClick(selected)
        }
    }
}
