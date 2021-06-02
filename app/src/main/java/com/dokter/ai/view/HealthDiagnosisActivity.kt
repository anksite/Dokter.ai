package com.dokter.ai.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestManager
import com.dokter.ai.R
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.data.network.ResponseQuestion
import com.dokter.ai.databinding.ActivityHealthDiagnosisBinding
import com.dokter.ai.databinding.SheetExitDiagnosisBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import com.dokter.ai.view.viewmodel.VMHealthDiagnosis
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HealthDiagnosisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealthDiagnosisBinding

    val vmHealthDiagnosis: VMHealthDiagnosis by viewModels()

    val mAllSymptom: List<DataSymptom> by lazy {
        intent.getParcelableArrayListExtra(Cons.ALL_SYMPTOM)
    }

    lateinit var mDataSymptom: DataSymptom

    lateinit var resultDiagnosis: ResponseQuestion

    @Inject
    lateinit var mSpHelp: SpHelp
    @Inject
    lateinit var mGlide: RequestManager

    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Diagnosa Penyakit"

        hideView()
        vmHealthDiagnosis.getNextQuestion()

        binding.let {
            it.tvYes.setOnClickListener {
                if (!isLoading) {
                    vmHealthDiagnosis.setAnswerGetQuestion(mDataSymptom.id, 1)
                    (it as TextView).apply {
                        background = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.bg_symptom_selected
                        )
                        setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                        setTypeface(null, Typeface.BOLD)
                    }
                }

            }

            it.tvNo.setOnClickListener {
                if(!isLoading){
                    vmHealthDiagnosis.setAnswerGetQuestion(mDataSymptom.id, 0)
                    (it as TextView).apply {
                        background = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.bg_symptom_selected
                        )
                        setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                        setTypeface(null, Typeface.BOLD)
                    }
                }
            }
        }

        binding.clInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(Cons.DATA_SYMPTOM, mDataSymptom)

            val dialog =
                ChooseSymptomActivity.BottomSheetSymptomDetail(null, null, mGlide)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, dialog.tag)
        }

        vmHealthDiagnosis.let {
            it.question.observe({ lifecycle }, {
                with(binding) {
                    if (it.result_state == 0) {
                        mDataSymptom = getDataSymptomFromList(it.question_id)
                        tvQuestion.text = mDataSymptom.question
                    } else {
                        hideView()
                        resultDiagnosis = it
                        val jsonBody = JsonObject()
                        jsonBody.addProperty("id", mSpHelp.getString(Cons.ID_USER))
                        jsonBody.addProperty("symptoms", "mual, pusing, sakit kepala")
                        jsonBody.addProperty("illness", it.disease_id)
                        jsonBody.addProperty("akurasi", getAccuracy(it.probability))

                        vmHealthDiagnosis.saveHistory(jsonBody)
                    }
                }
            })

            it.state.observe({ lifecycle }, {
                when (it) {
                    Cons.STATE_LOADING -> {
                        binding.pbLoad.visibility = View.VISIBLE
                        isLoading = true
                    }

                    Cons.STATE_SUCCESS -> {
                        showView()
                        binding.pbLoad.visibility = View.GONE
                        isLoading = false

                        binding.tvNo.apply {
                            background =
                                ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }

                        binding.tvYes.apply {
                            background =
                                ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }
                    }

                    Cons.STATE_ERROR -> {
                        binding.pbLoad.visibility = View.GONE
                        isLoading = false
                        Toast.makeText(
                            this,
                            "Jaringan bermasalah, Silakan coba lagi",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.tvNo.apply {
                            background =
                                ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }

                        binding.tvYes.apply {
                            background =
                                ContextCompat.getDrawable(applicationContext, R.drawable.bg_symptom)
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                            setTypeface(null, Typeface.NORMAL)
                        }
                    }

                    Cons.STATE_SAVE_HISTORY_SUCCESS -> {
                        val i = Intent(applicationContext, ResultDiagnosisActivity::class.java)
                        i.putExtra(Cons.ID_DISEASE, resultDiagnosis.disease_id)
                        i.putExtra(Cons.PROBABILITY, getAccuracy(resultDiagnosis.probability))
                        startActivity(i)
                        finish()
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        BottomSheetExit().let {
            it.show(supportFragmentManager, it.tag)
        }
    }

    fun hideView() {
        binding.apply {
            cvQuestion.visibility = View.GONE
            tvYes.visibility = View.GONE
            tvNo.visibility = View.GONE
        }
    }

    fun showView() {
        binding.apply {
            cvQuestion.visibility = View.VISIBLE
            tvYes.visibility = View.VISIBLE
            tvNo.visibility = View.VISIBLE
        }
    }

    fun getDataSymptomFromList(idQuestion: String): DataSymptom {
        val search = mAllSymptom.filter {
            it.id == idQuestion
        }

        return if (search.isNotEmpty()) {
            search[0]
        } else {
            DataSymptom(idQuestion, "Belum diinput", "Belum diinput", "", idQuestion, false)
        }
    }

    fun getAccuracy(input: Float): Int {
        return (input * 100).toInt()
    }

    class BottomSheetExit : BottomSheetDialogFragment() {
        lateinit var mDialog: Dialog

        override fun setupDialog(dialog: Dialog, style: Int) {
            mDialog = dialog
            val binding = SheetExitDiagnosisBinding.inflate(LayoutInflater.from(context))
            binding.ivClose.setOnClickListener {
                dialog.cancel()
            }

            binding.bYes.setOnClickListener {
                activity?.finish()
            }

            binding.bNo.setOnClickListener {
                dialog.cancel()
            }

            dialog.setContentView(binding.root)
        }
    }
}