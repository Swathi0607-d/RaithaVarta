package com.raithavarta.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.raithavarta.app.data.FirebaseManager
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

/**
 * LoginActivity — shown whenever no user is logged-in.
 *
 * Modes:
 *  • Sign In  – user enters PIN only (PIN stored in SharedPrefs)
 *  • Sign Up  – new user creates name / district / crops / PIN
 *               Profile is also synced to Firestore (users/{uid}).
 *
 * On success → MainActivity.
 * ProfileFragment calls logOut() which clears the login flag and restarts here.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val PREFS = "raitha_prefs"
    private val KEY_NAME = "user_name"
    private val KEY_DISTRICT = "user_district"
    private val KEY_CROPS = "selected_crops"
    private val KEY_PIN = "user_pin"
    private val KEY_LOGGED_IN = "logged_in"

    /** True = show Sign-In form, False = show Sign-Up form */
    private var isSignInMode = true

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RaithaVarta)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Auto-detect: if no PIN saved yet, show sign-up; else show sign-in
        val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val hasSavedPin = !prefs.getString(KEY_PIN, null).isNullOrBlank()
        isSignInMode = hasSavedPin
        if (isSignInMode) showSignInUi() else showSignUpUi()

        binding.btnPrimary.setOnClickListener {
            if (isSignInMode) doSignIn() else doSignUp()
        }

        binding.btnSwitch.setOnClickListener {
            isSignInMode = !isSignInMode
            if (isSignInMode) showSignInUi() else showSignUpUi()
        }
    }

    // ── UI helpers ──────────────────────────────────────────────

    private fun showSignInUi() {
        binding.loginTitle.text = getString(R.string.login_title_signin)
        binding.loginSubtitle.text = getString(R.string.login_sub_signin)
        binding.fieldSignupName.visibility = View.GONE
        binding.fieldSignupDistrict.visibility = View.GONE
        binding.fieldSignupCrops.visibility = View.GONE
        binding.fieldPin.hint = getString(R.string.login_pin_hint)
        binding.btnPrimary.text = getString(R.string.login_btn_signin)
        binding.btnSwitch.text = getString(R.string.login_btn_to_signup)
    }

    private fun showSignUpUi() {
        binding.loginTitle.text = getString(R.string.login_title_signup)
        binding.loginSubtitle.text = getString(R.string.login_sub_signup)
        binding.fieldSignupName.visibility = View.VISIBLE
        binding.fieldSignupDistrict.visibility = View.VISIBLE
        binding.fieldSignupCrops.visibility = View.VISIBLE
        binding.fieldPin.hint = getString(R.string.login_new_pin_hint)
        binding.btnPrimary.text = getString(R.string.login_btn_signup)
        binding.btnSwitch.text = getString(R.string.login_btn_to_signin)
    }

    // ── Auth logic ───────────────────────────────────────────────

    private fun doSignIn() {
        val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val savedPin = prefs.getString(KEY_PIN, null)

        if (savedPin.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.login_no_account), Toast.LENGTH_SHORT).show()
            isSignInMode = false
            showSignUpUi()
            return
        }

        val enteredPin = binding.fieldPin.text.toString().trim()
        if (enteredPin.isEmpty()) {
            Toast.makeText(this, getString(R.string.login_enter_pin), Toast.LENGTH_SHORT).show()
            return
        }

        if (enteredPin == savedPin) {
            prefs.edit().putBoolean(KEY_LOGGED_IN, true).apply()
            // Re-ensure Firebase session on sign-in (handles cold-start / token expiry)
            lifecycleScope.launch {
                try { FirebaseManager.ensureSignedIn() } catch (_: Exception) {}
                goToMain()
            }
        } else {
            Toast.makeText(this, getString(R.string.login_wrong_pin), Toast.LENGTH_SHORT).show()
        }
    }

    private fun doSignUp() {
        val name     = binding.fieldSignupName.text.toString().trim()
        val district = binding.fieldSignupDistrict.text.toString().trim()
        val crops    = binding.fieldSignupCrops.text.toString().trim()
        val pin      = binding.fieldPin.text.toString().trim()

        if (name.isEmpty() || district.isEmpty()) {
            Toast.makeText(this, getString(R.string.login_fill_required), Toast.LENGTH_SHORT).show()
            return
        }
        if (pin.length < 4) {
            Toast.makeText(this, getString(R.string.login_pin_length), Toast.LENGTH_SHORT).show()
            return
        }

        val cropsValue = crops.ifBlank { "Paddy, Tomato" }

        // 1. Save locally first so the app works offline
        getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().apply {
            putString(KEY_NAME, name)
            putString(KEY_DISTRICT, district)
            putString(KEY_CROPS, cropsValue)
            putString(KEY_PIN, pin)
            putBoolean(KEY_LOGGED_IN, true)
        }.apply()

        // 2. Sync profile to Firestore in the background (best-effort)
        lifecycleScope.launch {
            try {
                FirebaseManager.ensureSignedIn()
                FirebaseManager.saveUserProfile(
                    name     = name,
                    district = district,
                    crops    = cropsValue,
                    pinHash  = pin.hashCode().toString()   // never store plain PIN
                )
            } catch (_: Exception) {
                // Firestore sync failed (offline etc.) — local data is still intact
            }
            goToMain()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
