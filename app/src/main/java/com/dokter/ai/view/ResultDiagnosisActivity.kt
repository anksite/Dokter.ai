package com.dokter.ai.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.dokter.ai.data.network.ResponseDisease
import com.dokter.ai.databinding.ActivityResultDiagnosisBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.view.viewmodel.VMResultDiagnosis
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
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

        binding.bDone.setOnClickListener {
            finish()
        }

        vmResultDiagnosis.let { it ->
            it.disease.observe({lifecycle}, {
                Log.d("Result", "$it")
                if(it.contains("No Disease Document")){
                    binding.tvDisease.text = it
                    binding.tvAcc.text = "Tingkat akurasi $probability"
                }else {
                    val dataDisease = Gson().fromJson(it, ResponseDisease::class.java)

                    binding.apply {
                        tvDisease.text = dataDisease.name
                        tvAcc.text = "Tingkat akurasi $probability"
                        tvDesc.text = dataDisease.description
                        tvRecom.text = dataDisease.recomendation.toString()

                        try{
                            mGlide.load(dataDisease.image).into(ivDisease)
                        }catch (e: Exception){
                            e.printStackTrace()
                            ivDisease.visibility = View.GONE
                            Toast.makeText(applicationContext, "Failed load image", Toast.LENGTH_SHORT).show()
                        }
                    }
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