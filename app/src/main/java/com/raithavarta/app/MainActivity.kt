package com.raithavarta.app

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.databinding.ActivityMainBinding
import com.raithavarta.app.ui.diagnose.DiagnoseFragment
import com.raithavarta.app.ui.home.HomeFragment
import com.raithavarta.app.ui.profile.ProfileFragment
import com.raithavarta.app.ui.tips.TipsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) switchTo(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener { item ->
            if (navigatingProgrammatically) return@setOnItemSelectedListener true
            val frag: Fragment = when (item.itemId) {
                R.id.nav_home     -> HomeFragment()
                R.id.nav_tips     -> TipsFragment()
                R.id.nav_diagnose -> DiagnoseFragment()
                R.id.nav_profile  -> ProfileFragment()
                else              -> HomeFragment()
            }
            switchTo(frag)
            true
        }
    }

    /** Set to true while we are updating selectedItemId programmatically so the
     *  OnItemSelectedListener does not fire a second fragment transaction. */
    private var navigatingProgrammatically = false

    /** Navigate programmatically and also highlight the correct bottom-nav item */
    fun navigateTo(fragment: Fragment, navItemId: Int) {
        switchTo(fragment)
        navigatingProgrammatically = true
        binding.bottomNav.selectedItemId = navItemId
        navigatingProgrammatically = false
    }

    private fun switchTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
