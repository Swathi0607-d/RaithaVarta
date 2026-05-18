package com.raithavarta.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    private val PREFS = "raitha_prefs"
    private val KEY_NAME = "user_name"
    private val KEY_DISTRICT = "user_district"
    private val KEY_CROPS = "selected_crops"
    private val KEY_PROFILE_SET = "profile_set"

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RaithaVarta)
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.fieldName.text.toString().trim()
            val district = binding.fieldDistrict.text.toString().trim()
            val crops = binding.fieldCrops.text.toString().trim()

            if (name.isEmpty() || district.isEmpty()) {
                Toast.makeText(this, "Please enter your name and district", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().apply {
                putString(KEY_NAME, name)
                putString(KEY_DISTRICT, district)
                putString(KEY_CROPS, crops.ifBlank { "Paddy, Tomato" })
                putBoolean(KEY_PROFILE_SET, true)
            }.apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnReturning.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
