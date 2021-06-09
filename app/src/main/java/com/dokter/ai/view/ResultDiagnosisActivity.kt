package com.dokter.ai.view

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dokter.ai.R
import com.dokter.ai.data.local.entity.EntityHistory
import com.dokter.ai.data.network.ResponseDisease
import com.dokter.ai.databinding.ActivityResultDiagnosisBinding
import com.dokter.ai.databinding.SheetDisclaimerBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.view.adapter.RecyclerAdapterRecom
import com.dokter.ai.view.viewmodel.VMResultDiagnosis
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ResultDiagnosisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultDiagnosisBinding

    val vmResultDiagnosis: VMResultDiagnosis by viewModels()
    @Inject
    lateinit var mGlide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDiagnosisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Hasil Diagnosa"

        val idDisease = intent.getStringExtra(Cons.ID_DISEASE)
        val probability = intent.getIntExtra(Cons.PROBABILITY, -1)
        val symptoms = intent.getStringArrayListExtra(Cons.SYMPTOMS)

        vmResultDiagnosis.getResultDisease(idDisease)

        binding.bDone.setOnClickListener {
            finish()
        }

        vmResultDiagnosis.let { it ->
            it.disease.observe({ lifecycle }, {
                Log.d("Result", "$it")
                if (it.contains("No Disease Document")) {
                    binding.tvDisease.text = it
                    binding.tvAcc.text = "Tingkat akurasi $probability%"
                } else {
                    val dataDisease = Gson().fromJson(it, ResponseDisease::class.java)

                    binding.apply {
                        tvDisease.text = dataDisease.name
                        tvAcc.text = "Tingkat akurasi $probability%"
                        tvDesc.text = dataDisease.description

                        rvRecom.apply {
                            layoutManager = object : LinearLayoutManager(applicationContext) {
                                override fun canScrollVertically(): Boolean {
                                    return false
                                }
                            }

                            adapter = RecyclerAdapterRecom(dataDisease.recomendation)
                        }

                        mGlide
                            .load(dataDisease.image).centerCrop()
                            .listener(object : RequestListener<Drawable?> {

                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable?>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    ivDisease.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable?>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }
                            })
                            .into(ivDisease)

                    }

                    val c = Calendar.getInstance()
                    val sdf = SimpleDateFormat("yyy-MM-dd HH:mm")

                    val entityHistory = EntityHistory(
                        0,
                        sdf.format(c.time),
                        symptoms.toString(),
                        dataDisease.name,
                        probability,
                        dataDisease.image,
                        dataDisease.description,
                        dataDisease.recomendation.toString()
                    )

                    vmResultDiagnosis.saveHistoryLocal(entityHistory)
                }

            })

            it.state.observe({ lifecycle }, {
                when (it) {
                    Cons.STATE_LOADING -> {
                        binding.pbLoad.visibility = View.VISIBLE
                    }

                    Cons.STATE_SUCCESS -> {
                        binding.pbLoad.visibility = View.GONE
                        BottomSheetDisclaimer().let {
                            it.show(supportFragmentManager, it.tag)
                        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_info -> {
                BottomSheetDisclaimer().let {
                    it.show(supportFragmentManager, it.tag)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class BottomSheetDisclaimer : BottomSheetDialogFragment() {
        lateinit var mDialog: Dialog

        override fun setupDialog(dialog: Dialog, style: Int) {
            mDialog = dialog
            val binding = SheetDisclaimerBinding.inflate(LayoutInflater.from(context))
            binding.ivClose.setOnClickListener {
                dialog.cancel()
            }
            dialog.setContentView(binding.root)
        }
    }
}