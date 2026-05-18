package com.raithavarta.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.raithavarta.app.data.FirebaseManager
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.databinding.ActivitySplashBinding
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val PREFS = "raitha_prefs"
    private val KEY_LOGGED_IN = "logged_in"

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RaithaVarta)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashBtn.setOnClickListener {
            // Ensure Firebase anonymous session exists before navigating
            binding.splashBtn.isEnabled = false
            lifecycleScope.launch {
                try {
                    FirebaseManager.ensureSignedIn()
                } catch (e: Exception) {
                    // Non-fatal: proceed even if offline — local prefs still work
                }
                val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                val isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false)
                if (isLoggedIn) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }
}
