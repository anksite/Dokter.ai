package com.dokter.ai.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dokter.ai.R
import com.dokter.ai.data.local.entity.EntityHistory
import com.dokter.ai.databinding.RowListHistoryBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.view.HistoryDetailActivity
import java.text.SimpleDateFormat

class RecyclerAdapterHistory(val listHistory: List<EntityHistory>, val activity: Activity): RecyclerView.Adapter<RecyclerAdapterHistory.VHSHistory>() {
    lateinit var binding: RowListHistoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHSHistory {
        binding = RowListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHSHistory(binding)
    }

    override fun onBindViewHolder(holder: VHSHistory, position: Int) {
        val dataHistory = listHistory[position]

        holder.binding.tvDisease.text = dataHistory.disease
        holder.binding.tvDate.text = getDisplayDate(dataHistory.date)
        Glide.with(activity.applicationContext).load(dataHistory.img).placeholder(R.drawable.square_grey).into(holder.binding.ivHistory)

        holder.itemView.setOnClickListener { it ->
            Intent(it.context, HistoryDetailActivity::class.java).also {
                it.putExtra(Cons.ENTITY_HISTORY, listHistory[position])
                activity.startActivity(it)
            }
        }
    }

    override fun getItemCount(): Int = listHistory.size

    class VHSHistory(b: RowListHistoryBinding): RecyclerView.ViewHolder(b.root){
        var binding = b
    }

    fun getDisplayDate(strDate:String):String{
        val date = SimpleDateFormat("yyy-MM-dd").parse(strDate)
        val display = SimpleDateFormat("dd MMMM yyy").format(date)
        return display
    }
}