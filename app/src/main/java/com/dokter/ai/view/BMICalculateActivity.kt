package com.dokter.ai.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityBmicalculateBinding

class BMICalculateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmicalculateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmicalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}