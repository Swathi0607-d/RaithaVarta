package com.raithavarta.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Singleton Room database for the app.
 * Currently stores: DiagnosisEntity (diagnosis history).
 */
@Database(entities = [DiagnosisEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun diagnosisDao(): DiagnosisDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "raitha_varta_db"
                ).build().also { INSTANCE = it }
            }
    }
}
