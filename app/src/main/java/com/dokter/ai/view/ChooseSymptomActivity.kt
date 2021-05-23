package com.dokter.ai.view

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityChooseSymptomBinding
import com.dokter.ai.databinding.SheetDetailSymptomBinding
import com.dokter.ai.util.Cons
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class ChooseSymptomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseSymptomBinding
    lateinit var mAdapter: RecyclerAdapterSymptom
    val mListSymptom = mutableListOf<DataSymptom>()
    val mAllSymptom = getListSymptom()
    var mIndexSelected = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseSymptomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Pilih Gejala"

        mListSymptom.addAll(mAllSymptom)
        mAdapter = RecyclerAdapterSymptom(mListSymptom, iRvClick)

        binding.rvSymptom.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = mAdapter
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
        listSymptom.apply {
            add(DataSymptom(1, "Batuk", "Batuk deskripsi", "null", false))
            add(DataSymptom(2, "Kejang", "Kejang deskripsi", "null", false))
            add(DataSymptom(3, "Capek", "Capek deskripsi", "null", false))
            add(DataSymptom(4, "Kesemutan", "Kesemutan deskripsi", "null", false))
            add(DataSymptom(5, "Nyeri", "Nyeri deskripsi", "null", false))
            add(DataSymptom(6, "Memar", "Memar deskripsi", "null", false))
        }
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