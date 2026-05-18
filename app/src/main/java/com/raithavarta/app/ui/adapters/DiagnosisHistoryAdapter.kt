package com.raithavarta.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raithavarta.app.data.db.DiagnosisEntity
import com.raithavarta.app.databinding.ItemDiagnosisHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adapter for the diagnosis history list in DiagnoseFragment.
 */
class DiagnosisHistoryAdapter :
    ListAdapter<DiagnosisEntity, DiagnosisHistoryAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDiagnosisHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(private val b: ItemDiagnosisHistoryBinding) : RecyclerView.ViewHolder(b.root) {
        private val fmt = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

        fun bind(item: DiagnosisEntity) {
            b.historyEmoji.text   = item.cropEmoji
            b.historyCrop.text    = item.cropName
            b.historyDate.text    = fmt.format(Date(item.timestamp))
            b.historyResult.text  = item.resultText.lines()
                .firstOrNull { it.contains("🔬") }
                ?: item.resultText.take(80)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<DiagnosisEntity>() {
            override fun areItemsTheSame(a: DiagnosisEntity, b: DiagnosisEntity) = a.id == b.id
            override fun areContentsTheSame(a: DiagnosisEntity, b: DiagnosisEntity) = a == b
        }
    }
}
