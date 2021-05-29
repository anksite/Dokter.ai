package com.dokter.ai.view

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.MonthDisplayHelper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dokter.ai.R
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.data.network.ResponseQuestion
import com.dokter.ai.databinding.ActivityHealthDiagnosisBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import com.dokter.ai.view.viewmodel.VMHealthDiagnosis
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HealthDiagnosisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealthDiagnosisBinding

    val vmHealthDiagnosis: VMHealthDiagnosis by viewModels()

    val mAllSymptom : List<DataSymptom> by lazy {
        intent.getParcelableArrayListExtra(Cons.ALL_SYMPTOM)
    }

    var mIdSymptom = "-1"

    lateinit var resultDiagnosis: ResponseQuestion

    @Inject lateinit var mSpHelp : SpHelp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Diagnosa Penyakit"

        vmHealthDiagnosis.getNextQuestion()

        binding.let {
            it.tvYes.setOnClickListener {
                vmHealthDiagnosis.setAnswerGetQuestion(mIdSymptom, 1)
                (it as TextView).apply {
                    background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom_selected)
                    setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                    setTypeface(null, Typeface.BOLD)
                }
            }

            it.tvNo.setOnClickListener {
                vmHealthDiagnosis.setAnswerGetQuestion(mIdSymptom, 0)
                (it as TextView).apply {
                    background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom_selected)
                    setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                    setTypeface(null, Typeface.BOLD)
                }
            }
        }

        binding.clInfo.setOnClickListener {

        }

        vmHealthDiagnosis.let {
            it.question.observe({lifecycle}, {
                with(binding){
                    if(it.result_state==0){
                        mIdSymptom = it.question_id
                        tvQuestion.text = getQuestionFromList(mIdSymptom)
                    } else {
                        resultDiagnosis = it
                        val jsonBody = JsonObject()
                        jsonBody.addProperty("id", mSpHelp.getString(Cons.ID_USER))
                        jsonBody.addProperty("symptoms", "mual, pusing, sakit kepala")
                        jsonBody.addProperty("illness", it.disease_id)
                        jsonBody.addProperty("akurasi", 98)

                        vmHealthDiagnosis.saveHistory(jsonBody)
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

                        binding.tvNo.apply {
                            background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }

                        binding.tvYes.apply {
                            background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }
                    }

                    Cons.STATE_ERROR -> {
                        Toast.makeText(
                            this,
                            "Jaringan bermasalah, Silakan coba lagi",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.tvNo.apply {
                            background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }

                        binding.tvYes.apply {
                            background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }
                    }

                    Cons.STATE_SAVE_HISTORY_SUCCESS -> {
                        val i = Intent(applicationContext, ResultDiagnosisActivity::class.java)
                        i.putExtra(Cons.ID_DISEASE, resultDiagnosis.disease_id)
                        i.putExtra(Cons.PROBABILITY, resultDiagnosis.probability)
                        startActivity(i)
                        finish()
                    }
                }
            })
        }
    }

    fun getQuestionFromList(idQuestion: String): String {
        val search = mAllSymptom.filter {
            it.id == idQuestion
        }

        return if(search.isNotEmpty()) {
            search[0].question
        }else{
            idQuestion
        }
    }
}