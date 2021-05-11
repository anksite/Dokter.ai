package com.dokter.ai.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityResultDiagnosisBinding

class ResultDiagnosisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultDiagnosisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}