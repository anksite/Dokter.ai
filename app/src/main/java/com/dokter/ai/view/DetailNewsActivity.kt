package com.dokter.ai.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dokter.ai.databinding.ActivityDetailNewsBinding
import com.dokter.ai.view.model.NewsEntity
import com.dokter.ai.view.util.DataDummy

class DetailNewsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE = "extra course"
    }

    private lateinit var binding : ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Daily News"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val courseId = extras.getString(EXTRA_COURSE)
            if (courseId != null) {
                for (course in DataDummy.generateNewsData()) {
                    if (course.title == courseId) {
                        dataDetail(course)
                    }
                }
            }

        }
    }

    private fun dataDetail(data: NewsEntity) {
        binding.titleCatalogue.text = data.title
        binding.rateCatalogue.text = data.description
        Glide.with(this)
            .load(data.photo).centerCrop()
            .into(binding.imagePhoto)
    }

}