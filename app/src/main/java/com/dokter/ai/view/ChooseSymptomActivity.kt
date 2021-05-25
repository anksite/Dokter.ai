package com.dokter.ai.view

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.dokter.ai.R
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.databinding.ActivityChooseSymptomBinding
import com.dokter.ai.databinding.SheetDetailSymptomBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.view.viewmodel.VMChooseSymptom
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChooseSymptomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseSymptomBinding
    lateinit var mAdapter: RecyclerAdapterSymptom
    val mListSymptom = mutableListOf<DataSymptom>()
    lateinit var mAllSymptom : List<DataSymptom>
    var mIndexSelected = -1

    val vmChooseSymptom:VMChooseSymptom by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseSymptomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Pilih Gejala"

        mAdapter = RecyclerAdapterSymptom(mListSymptom, iRvClick)

        binding.rvSymptom.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = mAdapter
        }

        vmChooseSymptom.let {
            it.getSymptom()
            it.listSymptom.observe({lifecycle}, {
                binding.pbLoad.visibility = View.GONE
                mAllSymptom = it
                mListSymptom.addAll(mAllSymptom)
                mAdapter.notifyDataSetChanged()
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

    fun searchSymptom(key: String){
        val result = mAllSymptom.filter {
            it.name.toLowerCase(Locale.ROOT).contains(key)
        }

        mListSymptom.clear()
        mListSymptom.addAll(result)
        mAdapter.notifyDataSetChanged()
    }

    fun getListSymptom(): List<DataSymptom>{
        val listSymptom = mutableListOf<DataSymptom>()
//        listSymptom.apply {
//            add(DataSymptom(1, "Batuk", "Batuk deskripsi", "null", false))
//            add(DataSymptom(2, "Kejang", "Kejang deskripsi", "null", false))
//            add(DataSymptom(3, "Capek", "Capek deskripsi", "null", false))
//            add(DataSymptom(4, "Kesemutan", "Kesemutan deskripsi", "null", false))
//            add(DataSymptom(5, "Nyeri", "Nyeri deskripsi", "null", false))
//            add(DataSymptom(6, "Memar", "Memar deskripsi", "null", false))
//        }
        return listSymptom
    }

    val iRvClick = object :RecyclerAdapterSymptom.IRvClick{
        override fun onItemClick(position: Int) {
            mIndexSelected = position

            val bundle = Bundle()
            bundle.putParcelable(Cons.DATA_SYMPTOM, mListSymptom[mIndexSelected])

            val dialog = BottomSheetSymptomDetail(iSheetCancel)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, dialog.tag)

            mListSymptom[mIndexSelected].selected = true
            mAdapter.notifyItemChanged(position)
        }
    }

    val iSheetCancel = object:BottomSheetSymptomDetail.ISheetCancel{
        override fun onCancel() {
            mListSymptom[mIndexSelected].selected = false
            mAdapter.notifyItemChanged(mIndexSelected)
            mIndexSelected = -1
        }
    }

    class BottomSheetSymptomDetail(val iSheetCancel: ISheetCancel): BottomSheetDialogFragment(){
        lateinit var mDialog: Dialog
        override fun setupDialog(dialog: Dialog, style: Int) {
            mDialog = dialog
            val dataSymptom = arguments?.getParcelable<DataSymptom>(Cons.DATA_SYMPTOM)

            val binding = SheetDetailSymptomBinding.inflate(LayoutInflater.from(context))

            dataSymptom?.let {
                binding.apply {
                    tvSymptom.text = it.name
                    tvDesc.text = it.desc

                    ivClose.setOnClickListener {
                        dialog.cancel()
                    }

                    bChoose.setOnClickListener {
                        startActivity(Intent(context, HealthDiagnosisActivity::class.java))
                    }
                }
            }

            dialog.setContentView(binding.root)
        }

        override fun onCancel(dialog: DialogInterface) {
            super.onCancel(dialog)
            iSheetCancel.onCancel()
        }

        interface ISheetCancel{
            fun onCancel()
        }
    }
}