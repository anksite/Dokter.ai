package com.dokter.ai.view.slider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityOverviewBinding
import com.dokter.ai.view.LoginActivity

class OverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverviewBinding
    private val introSlideAdapter = IntroSlideAdapter(
        listOf(
            IntroSlide(
                "Doktor.AI",
                "Know more about yourself with us.",
                R.drawable.slidepict
            ),
            IntroSlide(
                "Doktor.AI",
                "Are your mass enough yet? Or need more nutrition? Check with us.",
                R.drawable.slidepict
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.introSliderViewPager.adapter = introSlideAdapter

        binding.bNext.setOnClickListener {
            if (binding.introSliderViewPager.currentItem + 1 < introSlideAdapter.itemCount) {
                binding.introSliderViewPager.currentItem += 1
            } else{
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
        }

        binding.skipTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            
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