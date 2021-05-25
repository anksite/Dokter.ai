package com.dokter.ai.data.network

data class ResponseSymptom(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    var question: String
)