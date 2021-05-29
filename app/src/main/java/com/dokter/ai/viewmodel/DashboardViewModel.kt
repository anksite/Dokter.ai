package com.dokter.ai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _textPeriksa = MutableLiveData<String>().apply {
        value = "Cek Kesehatan"
    }
    val textPeriksa: LiveData<String> = _textPeriksa

    private val _textBMI = MutableLiveData<String>().apply {
        value = "Cek BMI"
    }
    val textBMI: LiveData<String> = _textBMI




}