package com.dokter.ai.data.network

import retrofit2.Response
import retrofit2.http.*

interface InterfaceApi {
    @GET("DokterAIAPI/symptom/get/all")
    suspend fun symptom(): List<ResponseSymptom>

    @GET("DokterAIAPI/question/reset/{idUser}")
    suspend fun resetQuestionTree(
        @Path("idUser") idUser: String
    ): Response<String>

    @GET("DokterAIAPI/question/set/{idUser}/{idSymptom}")
    suspend fun setInitialSymptom(
        @Path("idUser") idUser: String,
        @Path("idSymptom") idSymptom: String
    ): Response<String>

    @GET("DokterAIAPI/question/get/{idUser}")
    suspend fun getNextQuestion(
        @Path("idUser") idUser: String
    ): List<ResponseQuestion>

    @POST("DokterAIAPI/question/set_symptoms/{idUser}")
    @FormUrlEncoded
    suspend fun setSymptomAnswer(
        @Path("idUser") idUser: String,
        @Field("symptoms_id") symptoms_id: String,
        @Field("response") response: Int
    ): Response<String>

    @GET("DokterAIAPI/disease/{idDisease}")
    suspend fun getResultDisease(
        @Path("idDisease") idDisease: String,
    ): Response<String>
}