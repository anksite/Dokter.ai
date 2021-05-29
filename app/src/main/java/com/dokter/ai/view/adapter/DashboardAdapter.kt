package com.dokter.ai.view.adapter

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dokter.ai.databinding.CardViewBinding
import com.dokter.ai.databinding.FragmentDashboardBinding
import com.dokter.ai.view.DetailNewsActivity
import com.dokter.ai.view.model.NewsEntity

class DashboardAdapter : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    private var listnews = ArrayList<NewsEntity>()

    fun setNews(news: List<NewsEntity>?){
        if (news == null) return
        this.listnews.clear()
        this.listnews.addAll(news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.ViewHolder {

        val itemsDashboardBinding = CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsDashboardBinding)
    }

    override fun onBindViewHolder(holder: DashboardAdapter.ViewHolder, position: Int) {
        val news = listnews[position]
        holder.bind(news)
    }

    override fun getItemCount(): Int = listnews.size

    inner class ViewHolder(private val binding: CardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (course : NewsEntity){
            binding.tvCard.text = course.title
            itemView.setOnClickListener{
                val intent = Intent(itemView.context, DetailNewsActivity::class.java)
                intent.putExtra(DetailNewsActivity.EXTRA_COURSE, course.title)
                itemView.context.startActivity(intent)
            }
        }
    }


}