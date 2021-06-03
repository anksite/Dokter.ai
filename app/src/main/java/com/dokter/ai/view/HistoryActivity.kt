package com.dokter.ai.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dokter.ai.databinding.ActivityHistoryBinding
import com.dokter.ai.view.adapter.RecyclerAdapterHistory
import com.dokter.ai.view.viewmodel.VMHistory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    val vmHistory: VMHistory by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Riwayat Diagnosa"

        vmHistory.getHistory().observe({ lifecycle }, {
            binding.gNoHistory.visibility = View.GONE

            binding.rvHistory.apply {
                layoutManager = LinearLayoutManager(this@HistoryActivity)
                adapter = RecyclerAdapterHistory(it, this@HistoryActivity)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}