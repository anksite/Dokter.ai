package com.dokter.ai.view.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dokter.ai.R

class IntroSlideAdapter(private val introSlide: List<IntroSlide>):
            RecyclerView.Adapter<IntroSlideAdapter.IntroSlideViewHolder>(){
    inner class IntroSlideViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private val textTitle = v.findViewById<TextView>(R.id.textTitle)
        private val textDescription = v.findViewById<TextView>(R.id.textDescription)
        private val imageIcon = v.findViewById<ImageView>(R.id.imageSlideIcon)

        fun bind(introSlide: IntroSlide){
            textTitle.text = introSlide.title
            textDescription.text = introSlide.description
            imageIcon.setImageResource(introSlide.icon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
       return IntroSlideViewHolder(
           LayoutInflater.from(parent.context).inflate(
               R.layout.slide_item_container,
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
           holder.bind(introSlide[position])
    }

    override fun getItemCount(): Int {
            return introSlide.size
    }
}