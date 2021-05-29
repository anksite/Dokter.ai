package com.dokter.ai.data.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface InterfaceApiCloud {

    @POST("historypush")
    suspend fun saveHistory(
        @Body rawJson: JsonObject
    ): Response<String>
}