package com.dokter.ai.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityBmicalculateBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BMICalculateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmicalculateBinding

    @Inject lateinit var mSpHelp: SpHelp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmicalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Hitung BMI"

        val berat = mSpHelp.getString(Cons.BERAT)
        val tinggi = mSpHelp.getString(Cons.TINGGI)

        if(berat.isNotEmpty()&&tinggi.isNotEmpty()){
            binding.etBerat.setText(berat)
            binding.edtinggi.setText(tinggi)
            calculateBMI()
        } else {
            binding.cvResult.visibility = View.GONE
        }

        binding.bthitung.setOnClickListener{
            //validasi nilai dari ET
            if (binding.etBerat.text.toString() == "") {
                binding.etBerat.error = "Field harus diisi!"
            } else if(binding.edtinggi.text.toString()== ""){
                binding.edtinggi.error = "Field harus diisi!"
            } else {
                calculateBMI()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun calculateBMI(){
        //mengambil nilai dari edit text dan ngubah ke double
        val strBerat = binding.etBerat.text.toString()
        val strTinggi = binding.edtinggi.text.toString()

        val berat = strBerat.toFloat()
        val tinggi = strTinggi.toFloat()

        //menghitung
        val hitung = berat / ((tinggi/100)*(tinggi/100))

        binding.tvResult.text = formatResult(hitung)

        binding.tvDescResult.apply {
            if (hitung < 18.5){
                text = "Berat dibawah standar"
                setTextColor(ContextCompat.getColor(applicationContext, R.color.below_standar))
            } else if ( hitung >= 18.5 && hitung <25){
                text = "Berat Badan Ideal"
                setTextColor(ContextCompat.getColor(applicationContext, R.color.ideal))
            } else if ( hitung >= 25 && hitung <30){
                text = "Berat Badan Berlebih"
                setTextColor(ContextCompat.getColor(applicationContext, R.color.over))
            } else {
                text = "Berat Badan Terlalu Berlebih"
                setTextColor(ContextCompat.getColor(applicationContext, R.color.much_over))
            }
        }

        binding.cvResult.visibility = View.VISIBLE

        mSpHelp.writeString(Cons.BERAT, strBerat)
        mSpHelp.writeString(Cons.TINGGI, strTinggi)

        binding.etBerat.isEnabled = false
        binding.etBerat.isEnabled = true

        binding.edtinggi.isEnabled = false
        binding.edtinggi.isEnabled = true
    }

    fun formatResult(input:Float):String{
        val strInput = input.toString()
        if(strInput.contains(".")){
            val result = strInput.substring(0, strInput.indexOf(".")+2)
            return result
        }
        return strInput
    }

}


