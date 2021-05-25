package com.dokter.ai.view

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dokter.ai.R
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.databinding.RowListSymptomBinding

class RecyclerAdapterSymptom(val listSymptom: List<DataSymptom>, val iRvClick: IRvClick): RecyclerView.Adapter<RecyclerAdapterSymptom.VHSymptom>() {
    lateinit var binding: RowListSymptomBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHSymptom {
        binding = RowListSymptomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHSymptom(binding)
    }

    override fun onBindViewHolder(holder: VHSymptom, position: Int) {
        val dataSymptom = listSymptom[position]

        holder.binding.tvSymptom.text = dataSymptom.name
        holder.itemView.setOnClickListener {
            iRvClick.onItemClick(position)
        }

        if(dataSymptom.selected){
            holder.binding.tvSymptom.apply {
                background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_symptom_selected)
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
                setTypeface(null, Typeface.BOLD)
            }
        } else {
            holder.binding.tvSymptom.apply {
                background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_symptom)
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    override fun getItemCount(): Int = listSymptom.size

    class VHSymptom(b: RowListSymptomBinding): RecyclerView.ViewHolder(b.root){
        var binding = b
    }

    interface IRvClick {
        fun onItemClick(position: Int)
    }
}