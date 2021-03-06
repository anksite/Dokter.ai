package com.dokter.ai.data

import android.util.Log
import com.dokter.ai.data.local.entity.EntityHistory
import com.dokter.ai.data.local.room.DaoHistory
import com.dokter.ai.data.network.*
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import com.google.gson.JsonObject


class RepositoryDiagnosis() {

    init {
        Log.d("Repo", "Instance created")
    }

    suspend fun getAllSymptom(interfaceApi: InterfaceApi): ResultWrapper<List<DataSymptom>> {
        try {
            val result = interfaceApi.symptom()
            return ResultWrapper.Success(DataMapper.mapResponseToDomain(result))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultWrapper.Error
        }

        return ResultWrapper.Error
    }

    suspend fun resetQuestionTree(interfaceApi: InterfaceApi, idUSer: String): ResultWrapper<String> {
        try {
            val response = interfaceApi.resetQuestionTree(idUSer)
            if (response.isSuccessful) {
                response.body()?.let {
                    return ResultWrapper.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultWrapper.Error
        }

        return ResultWrapper.Error
    }

    suspend fun setInitialSymptom(interfaceApi: InterfaceApi, idUSer: String, idSymptom: String): ResultWrapper<String> {
        try {
            val response = interfaceApi.setInitialSymptom(idUSer, idSymptom)
            if (response.isSuccessful) {
                response.body()?.let {
                    return ResultWrapper.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultWrapper.Error
        }

        return ResultWrapper.Error
    }

    suspend fun getNextQuestion(interfaceApi: InterfaceApi, idUSer: String): ResultWrapper<ResponseQuestion> {
        try {
            val response = interfaceApi.getNextQuestion(idUSer)
            if (response.isNotEmpty()) {
                response[0].let {
                    return ResultWrapper.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultWrapper.Error
        }

        return ResultWrapper.Error
    }

    suspend fun setSymptomAnswer(interfaceApi: InterfaceApi, idUSer: String, rawJson: JsonObject): ResultWrapper<String> {
        try {
            val result = interfaceApi.setSymptomAnswer(idUSer, rawJson)
            if (result.isSuccessful) {
                result.body()?.let {
                    return ResultWrapper.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultWrapper.Error
        }

        return ResultWrapper.Error
    }

    suspend fun getResultDisease(interfaceApi: InterfaceApi, idDisease: String): ResultWrapper<String> {
        try {
            val result = interfaceApi.getResultDisease(idDisease)
            if (result.isSuccessful) {
                result.body()?.let {
                    return ResultWrapper.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultWrapper.Error
        }

        return ResultWrapper.Error
    }

    suspend fun saveHistory(interfaceApiCloud: InterfaceApiCloud, rawJson: JsonObject): ResultWrapper<String> {
        try {
            val result = interfaceApiCloud.saveHistory(rawJson)
            if (result.isSuccessful) {
                result.body()?.let {
                    return ResultWrapper.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultWrapper.Error
        }

        return ResultWrapper.Error
    }

    suspend fun saveHistoryLocal(daoHistory: DaoHistory, entityHistory: EntityHistory){
        daoHistory.insertHistory(entityHistory)
    }

    fun selectHistoryLocal(daoHistory: DaoHistory) = daoHistory.selectListHistory()
}