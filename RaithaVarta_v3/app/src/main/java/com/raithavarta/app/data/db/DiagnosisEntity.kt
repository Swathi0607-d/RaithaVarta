package com.raithavarta.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity — one row per AI diagnosis result stored on device.
 */
@Entity(tableName = "diagnosis_history")
data class DiagnosisEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cropName: String,
    val cropEmoji: String,
    val resultText: String,
    val lang: String,
    val timestamp: Long = System.currentTimeMillis()
)
