package com.dokter.ai.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.databinding.ActivityHealthDiagnosisBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.view.viewmodel.VMHealthDiagnosis
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HealthDiagnosisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealthDiagnosisBinding

    val vmHealthDiagnosis: VMHealthDiagnosis by viewModels()

    val mAllSymptom : List<DataSymptom> by lazy {
        intent.getParcelableArrayListExtra(Cons.ALL_SYMPTOM)
    }

    var mIdSymptom = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vmHealthDiagnosis.getNextQuestion()

        binding.let {
            it.tvYes.setOnClickListener {
                vmHealthDiagnosis.setAnswerGetQuestion(mIdSymptom, 1)
            }

            it.tvNo.setOnClickListener {
                vmHealthDiagnosis.setAnswerGetQuestion(mIdSymptom, 0)
            }
        }

        vmHealthDiagnosis.let {
            it.question.observe({lifecycle}, {
                with(binding){
                    if(it.result_state==0){
                        mIdSymptom = it.question_id
                        tvQuestion.text = getQuestionFromList(mIdSymptom)
                    } else {
                        val i = Intent(applicationContext, ResultDiagnosisActivity::class.java)
                        i.putExtra(Cons.ID_DISEASE, it.disease_id)
                        i.putExtra(Cons.PROBABILITY, it.probability)
                        startActivity(i)
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