package com.dokter.ai.data.network

data class ResponseSymptom(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    var question: String
)