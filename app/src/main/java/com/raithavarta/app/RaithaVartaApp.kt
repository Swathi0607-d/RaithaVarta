package com.raithavarta.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.data.db.AppDatabase

class RaithaVartaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialise Firebase — must happen before any Firebase call
        FirebaseApp.initializeApp(this)

        LanguageManager.applySavedLanguage(this)

        // Eagerly initialise Room so the DB is ready before first use
        AppDatabase.getInstance(this)
    }
}
