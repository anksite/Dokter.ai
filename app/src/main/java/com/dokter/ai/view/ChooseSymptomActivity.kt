package com.dokter.ai.view

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.dokter.ai.R
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.databinding.ActivityChooseSymptomBinding
import com.dokter.ai.databinding.SheetDetailSymptomBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import com.dokter.ai.view.adapter.RecyclerAdapterSymptom
import com.dokter.ai.view.viewmodel.VMChooseSymptom
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChooseSymptomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseSymptomBinding
    lateinit var mAdapter: RecyclerAdapterSymptom
    val mListSymptom = mutableListOf<DataSymptom>()
    lateinit var mAllSymptom: List<DataSymptom>
    var mIndexSelected = -1

    val vmChooseSymptom: VMChooseSymptom by viewModels()

    @Inject
    lateinit var mGlide: RequestManager

    @Inject
    lateinit var mSpHelp: SpHelp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseSymptomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Pilih Gejala"

        mAdapter = RecyclerAdapterSymptom(mListSymptom, iRvClick)

        binding.rvSymptom.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = mAdapter
        }

        vmChooseSymptom.let { it ->
            it.getSymptom()
            it.listSymptom.observe({ lifecycle }, {
                binding.pbLoad.visibility = View.GONE
                mAllSymptom = it
                mListSymptom.addAll(mAllSymptom)
                mAdapter.notifyDataSetChanged()
            })

            it.prepareState.observe({ lifecycle }, {
                when (it) {
                    Cons.STATE_LOADING -> {
                        binding.pbLoad.visibility = View.VISIBLE
                    }

                    Cons.STATE_SUCCESS -> {
                        binding.pbLoad.visibility = View.GONE
                        val i = Intent(this, HealthDiagnosisActivity::class.java)
                        val arrayList = ArrayList<DataSymptom>()
                        arrayList.addAll(mAllSymptom)
                        Log.d("arrayList", "$arrayList")
                        i.putExtra(Cons.ALL_SYMPTOM, arrayList)
                        startActivity(i)
                        finish()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_choose_symptom, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(key: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchSymptom(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    fun searchSymptom(key: String) {
        val result = mAllSymptom.filter {
            it.name.toLowerCase(Locale.ROOT).contains(key)
        }

        mListSymptom.clear()
        mListSymptom.addAll(result)
        mAdapter.notifyDataSetChanged()
    }

    val iRvClick = object : RecyclerAdapterSymptom.IRvClick {
        override fun onItemClick(position: Int) {
            mIndexSelected = position

            val bundle = Bundle()
            bundle.putParcelable(Cons.DATA_SYMPTOM, mListSymptom[mIndexSelected])

            val dialog = BottomSheetSymptomDetail(iSheetCancel, iSheetChoose, mGlide)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, dialog.tag)

            mListSymptom[mIndexSelected].selected = true
            mAdapter.notifyItemChanged(position)
        }
    }

    val iSheetCancel = object : BottomSheetSymptomDetail.ISheetCancel {
        override fun onCancel() {
            mListSymptom[mIndexSelected].selected = false
            mAdapter.notifyItemChanged(mIndexSelected)
            mIndexSelected = -1
        }
    }

    val iSheetChoose = object : BottomSheetSymptomDetail.ISheetChoose {
        override fun onChoose(idSymptom: String) {
            vmChooseSymptom.prepareDiagnosis(idSymptom)
        }
    }

    class BottomSheetSymptomDetail(
        val iSheetCancel: ISheetCancel?,
        val iSheetChoose: ISheetChoose?,
        val glide: RequestManager
    ) : BottomSheetDialogFragment() {
        lateinit var mDialog: Dialog

        override fun setupDialog(dialog: Dialog, style: Int) {
            mDialog = dialog
            val dataSymptom = arguments?.getParcelable<DataSymptom>(Cons.DATA_SYMPTOM)

            val binding = SheetDetailSymptomBinding.inflate(LayoutInflater.from(context))

            dataSymptom?.let {
                binding.apply {
                    val idSymptom = it.id
                    tvSymptom.text = it.name
                    glide.load(it.img).centerCrop().into(ivSymptom)
                    tvDesc.text = it.desc

                    ivClose.setOnClickListener {
                        dialog.cancel()
                    }

                    if(iSheetChoose==null){
                        bChoose.visibility = View.GONE
                    } else {
                        bChoose.setOnClickListener {
                            iSheetChoose.onChoose(idSymptom)
                        }
                    }
                }
            }

            dialog.setContentView(binding.root)
        }

        override fun onCancel(dialog: DialogInterface) {
            super.onCancel(dialog)
            iSheetCancel?.onCancel()
        }

        interface ISheetCancel {
            fun onCancel()
        }

        interface ISheetChoose {
            fun onChoose(idSymptom: String)
        }
    }
}