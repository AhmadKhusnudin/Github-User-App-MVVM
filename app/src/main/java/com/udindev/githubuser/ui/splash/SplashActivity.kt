package com.udindev.githubuser.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.udindev.githubuser.databinding.ActivitySplashBinding
import com.udindev.githubuser.ui.MainActivity
import com.udindev.githubuser.ui.setting.SettingPreferences
import com.udindev.githubuser.ui.setting.SettingViewModel
import com.udindev.githubuser.ui.setting.SettingViewModelFactory
import com.udindev.githubuser.ui.setting.dataStore

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val viewModelFactory = SettingViewModelFactory(pref)
        val viewModel: SettingViewModel =
            ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]

        viewModel.getThemeSetting().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500)

    }
}