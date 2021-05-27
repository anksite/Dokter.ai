package com.dokter.ai.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityResultDiagnosisBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.view.viewmodel.VMResultDiagnosis
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultDiagnosisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultDiagnosisBinding

    val vmResultDiagnosis: VMResultDiagnosis by viewModels()
    @Inject lateinit var mGlide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idDisease = intent.getStringExtra(Cons.ID_DISEASE)
        val probability = intent.getFloatExtra(Cons.PROBABILITY, -1f)

        vmResultDiagnosis.getResultDisease(idDisease)

        vmResultDiagnosis.let {
            it.disease.observe({lifecycle}, {
                Log.d("Result", "$it")
                binding.apply {
                    tvDisease.text = it.name
                    tvAcc.text = "Tingkat akurasi $probability"
                    mGlide.load(it.image).into(ivDisease)
                    tvDesc.text = it.description
                    tvRecom.text = it.recomendation.toString()
                }
            })

            it.state.observe({lifecycle}, {
                when (it) {
                    Cons.STATE_LOADING -> {
                        binding.pbLoad.visibility = View.VISIBLE
                    }

                    Cons.STATE_SUCCESS -> {
                        binding.pbLoad.visibility = View.GONE
                    }

                    Cons.STATE_ERROR -> Toast.makeText(
                        this,
                        "Jaringan bermasalah, Silakan coba lagi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }


    }
}