package com.dokter.ai.data.network

data class ResponseDisease (
    val image : String,
    val id : String,
    val description : String,
    val recomendation : List<String>,
    val name : String
)