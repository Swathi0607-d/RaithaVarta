package com.raithavarta.app.data

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {
    private const val PREFS = "raitha_varta_prefs"
    private const val KEY_LANG = "lang"

    fun getCurrentLang(context: Context): String {
        val sp = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return sp.getString(KEY_LANG, "en") ?: "en"
    }

    fun setLanguage(context: Context, langCode: String) {
        val sp = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        sp.edit().putString(KEY_LANG, langCode).apply()
        applyLocale(context.applicationContext, langCode)
    }

    fun applySavedLanguage(context: Context) {
        applyLocale(context, getCurrentLang(context))
    }

    private fun applyLocale(context: Context, langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)
    }

    fun wrapContext(context: Context): Context {
        val langCode = getCurrentLang(context)
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
