package com.dokter.ai.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dokter.ai.databinding.ActivitySplashBinding
import com.dokter.ai.view.slider.OverviewActivity

//import com.facebook.stetho.Stetho

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

       // Stetho.initializeWithDefaults(this)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OverviewActivity::class.java))
            finish()
        }, 2000)
    }
}