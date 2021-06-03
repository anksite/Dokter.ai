package com.dokter.ai.view.slider

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityOverviewBinding
import com.dokter.ai.view.LoginActivity
import com.dokter.ai.view.model.IntroSlide

class OverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverviewBinding
    private val introSlideAdapter = IntroSlideAdapter(
        listOf(
            IntroSlide(
                "Bagaimana kondisimu hari ini?",
                "Cek kesehatanmu hanya dengan menjawab pertanyaan Dokter.ai !",
                R.drawable.slidepict
            ),
            IntroSlide(
                "Sudahkah cek massa tubuhmu hari ini?",
                "Hitung sekarang menggunakan Dokter.ai !",
                R.drawable.slidepict
            )
        )
    )

    lateinit var activity: OverviewActivity
    lateinit var preferences: SharedPreferences
    val pref_show_intro = "intro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //to open once, not repeated
        activity = this
        preferences = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)
        preferences.getBoolean(pref_show_intro, true)

        if (!preferences.getBoolean(pref_show_intro, true)){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            
        }


        binding.introSliderViewPager.adapter = introSlideAdapter

        binding.bNext.setOnClickListener {
            if (binding.introSliderViewPager.currentItem + 1 < introSlideAdapter.itemCount) {
                binding.introSliderViewPager.currentItem += 1
            } else{
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                    val editor = preferences.edit()
                    editor.putBoolean(pref_show_intro, false )
                    editor.apply()
                }
            }
        }

        binding.skipTv.setOnClickListener {
            Intent(applicationContext, LoginActivity::class.java).also {
                startActivity(it)
                finish()
                val editor = preferences.edit()
                editor.putBoolean(pref_show_intro, false )
                editor.apply()
            }
        }

        binding.introSliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSlideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        application,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorsContainer.addView(indicators[i])
        }
        
    }

    private fun setCurrentIndicator(index: Int){
        val childCount = binding.indicatorsContainer.childCount
        for (i in 0 until childCount){
            val imageView = binding.indicatorsContainer.get(i) as ImageView
            if (i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.indicator_active
                ))
            }   else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.indicator_inactive ))
            }
        }
    }
}