package com.dokter.ai.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityBmicalculateBinding

class BMICalculateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmicalculateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmicalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pesan : String
        var edbb: EditText = findViewById(R.id.edbb)
        var edtinggi: EditText = findViewById(R.id.edtinggi)
        var edimt: TextView = findViewById(R.id.edimt)
        var edket: TextView = findViewById(R.id.edket)
        var bthitung: Button = findViewById(R.id.bthitung)
        var btnReset: Button = findViewById(R.id.btReset)

        bthitung.setOnClickListener{
            //validasi nilai dari ET
            if (edbb.text.toString() == "") {
                edbb.error = "Field harus diisi!"
                return@setOnClickListener
            }

            if(edtinggi.text.toString()== ""){
                edtinggi.error = "Field harus diisi!"
                return@setOnClickListener
            }

            //mengambil nilai dari edit text dan ngubah ke double
            val inputEdbb = edbb.text.toString().toDouble()
            val inputEdtinggi = edtinggi.text.toString().toDouble()

            //menghitung
            val hitung = inputEdbb / ((inputEdtinggi/100)*(inputEdtinggi/100))

            if (hitung < 18.5){
                pesan = "Berat dibawah standar"
            } else if ( hitung >= 18.5 && hitung <25){
                pesan = "Berat Badan Ideal"
            } else if ( hitung >= 25 && hitung <30){
                pesan = "Berat Badan Berlebih"
            } else {
                pesan = "Berat Badan Terlalu Berlebih"
            }

            edimt.setText(""+hitung)
            edket.setText(""+pesan)

        }

        btnReset.setOnClickListener{
            edbb.run { setText("") }
            edtinggi.setText("")
            edimt.setText("")
            edket.setText("")
        }

    }

}


