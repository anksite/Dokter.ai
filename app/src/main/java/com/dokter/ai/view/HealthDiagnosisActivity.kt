package com.dokter.ai.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dokter.ai.databinding.ActivityHealthDiagnosisBinding
import com.dokter.ai.databinding.ActivityOverviewBinding

class HealthDiagnosisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealthDiagnosisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}