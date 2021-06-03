package com.dokter.ai.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore.Video
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dokter.ai.data.local.entity.EntityHistory
import com.dokter.ai.databinding.ActivityHistoryDetailBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.view.adapter.RecyclerAdapterRecom
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDetailBinding
    @Inject lateinit var mGlide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Riwayat Diagnosa"

        val entityHistory = intent.getParcelableExtra<EntityHistory>(Cons.ENTITY_HISTORY)

        binding.apply {
            tvDate.text = getDisplayDate(entityHistory.date)
            rvGejala.apply {
                layoutManager = object : LinearLayoutManager(applicationContext) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }

                val listGejala = getListFromString(entityHistory.symptom)

                adapter = RecyclerAdapterRecom(listGejala)
            }

        }

        binding.apply {
            tvDisease.text = entityHistory.disease
            tvAcc.text = "Tingkat akurasi ${entityHistory.accuracy}%"
            tvDesc.text = entityHistory.diseaseDesc

            rvRecom.apply {
                layoutManager = object : LinearLayoutManager(applicationContext) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }

                val listRecom = getListFromString(entityHistory.recom)

                adapter = RecyclerAdapterRecom(listRecom)
            }

            mGlide
                .load(entityHistory.img).centerCrop()
                .listener(object : RequestListener<Drawable?> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ivDisease.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(ivDisease)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getListFromString(str: String): List<String>{
        var cleanStr = str.replace("[", "")
        cleanStr = cleanStr.replace("]", "")
        cleanStr = cleanStr.replace(", ", ",")

        val list = cleanStr.split(",")
        return list
    }

    fun getDisplayDate(strDate:String):String{
        val date = SimpleDateFormat("yyy-MM-dd").parse(strDate)
        val display = SimpleDateFormat("dd MMMM yyy").format(date)
        return display
    }
}