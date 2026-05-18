package com.raithavarta.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raithavarta.app.R
import com.raithavarta.app.data.StoryItem

class StoryAdapter(
    private val items: List<StoryItem>,
    private val lang: String
) : RecyclerView.Adapter<StoryAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: TextView = view.findViewById(R.id.storyAvatar)
        val name: TextView = view.findViewById(R.id.storyName)
        val loc: TextView = view.findViewById(R.id.storyLoc)
        val text: TextView = view.findViewById(R.id.storyText)
        val result: TextView = view.findViewById(R.id.storyResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val s = items[position]
        holder.avatar.text = s.avatar
        holder.name.text = if (lang == "kn") s.nameKn else s.nameEn
        holder.loc.text = if (lang == "kn") s.locKn else s.locEn
        holder.text.text = if (lang == "kn") s.textKn else s.textEn
        holder.result.text = if (lang == "kn") s.resultKn else s.resultEn
    }
}
