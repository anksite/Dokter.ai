package com.dokter.ai.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dokter.ai.databinding.RowListRecomBinding

class RecyclerAdapterRecom(val listRecom: List<String>): RecyclerView.Adapter<RecyclerAdapterRecom.VHRecom>() {
    lateinit var binding: RowListRecomBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHRecom {
        binding = RowListRecomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHRecom(binding)
    }

    override fun onBindViewHolder(holder: VHRecom, position: Int) {
        holder.binding.tvRecom.text = listRecom[position]
    }

    override fun getItemCount(): Int = listRecom.size

    class VHRecom(b: RowListRecomBinding): RecyclerView.ViewHolder(b.root){
        var binding = b
    }

}