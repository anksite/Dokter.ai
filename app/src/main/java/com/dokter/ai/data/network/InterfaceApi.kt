package com.dokter.ai.data.network

import retrofit2.http.GET

interface InterfaceApi {
    @GET("/DokterAIAPI/symptom/get/all")
    suspend fun symptom(): List<ResponseSymptom>
}