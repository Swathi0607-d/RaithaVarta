package com.raithavarta.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object for diagnosis history.
 */
@Dao
interface DiagnosisDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisEntity)

    /** Returns the 10 most recent diagnoses, newest first. LiveData so UI auto-updates. */
    @Query("SELECT * FROM diagnosis_history ORDER BY timestamp DESC LIMIT 10")
    fun getRecentDiagnoses(): LiveData<List<DiagnosisEntity>>

    @Query("DELETE FROM diagnosis_history")
    suspend fun clearAll()
}
