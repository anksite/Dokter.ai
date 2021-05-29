package com.dokter.ai.data.network

data class ResponseQuestion(
    val question_id: String,
    val result_state: Int,
    val disease_id: String,
    val probability: Float
)