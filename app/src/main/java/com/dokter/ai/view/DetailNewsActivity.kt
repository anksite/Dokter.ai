package com.dokter.ai.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dokter.ai.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dokter.ai.databinding.ActivityDetailNewsBinding
import com.dokter.ai.view.model.NewsEntity
import com.dokter.ai.view.util.DataDummy

class DetailNewsActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_COURSE = "extra course"
    }
    private lateinit var activityDetailNewsBinding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_news)

        activityDetailNewsBinding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(activityDetailNewsBinding.root)

        supportActionBar?.title = "Daily News"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if(extras != null){
            val courseId = extras.getString(EXTRA_COURSE)
            if (courseId != null){
            for (course in DataDummy.generateNewsData()){
                if (course.title == courseId ){
                    dataDetail(course)
                }
            }
            }

        }
    }

    private fun dataDetail(data: NewsEntity){
        activityDetailNewsBinding.titleCatalogue.text = data.title
        activityDetailNewsBinding.rateCatalogue.text = data.description
        Glide.with(this)
            .load(data.photo)
            .apply(RequestOptions().override(100, 300))
            .into(activityDetailNewsBinding.imagePhoto)
    }

}