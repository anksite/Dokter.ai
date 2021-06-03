package com.dokter.ai.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokter.ai.data.RepositoryDiagnosis
import com.dokter.ai.data.local.entity.EntityHistory
import com.dokter.ai.data.local.room.DaoHistory
import com.dokter.ai.data.network.InterfaceApi
import com.dokter.ai.data.network.InterfaceApiCloud
import com.dokter.ai.data.network.ResponseQuestion
import com.dokter.ai.data.network.ResultWrapper
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMHealthDiagnosis @Inject constructor(val repositoryDiagnosis: RepositoryDiagnosis): ViewModel() {
    @Inject lateinit var interfaceApi: InterfaceApi
    @Inject lateinit var interfaceApiCloud: InterfaceApiCloud
    @Inject lateinit var mSpHelp: SpHelp

    val mQuestion = MutableLiveData<ResponseQuestion>()
    val question : LiveData<ResponseQuestion> = mQuestion

    val mState = MutableLiveData<String>()
    val state : LiveData<String> = mState

    fun getNextQuestion() {
        mState.postValue(Cons.STATE_LOADING)
        viewModelScope.launch {
            when(val result = repositoryDiagnosis.getNextQuestion(interfaceApi, mSpHelp.getString(Cons.ID_USER))){
                is ResultWrapper.Success -> {
                    mState.postValue(Cons.STATE_SUCCESS)
                    mQuestion.postValue(result.value)
                }
                is ResultWrapper.Error -> mState.postValue(Cons.STATE_ERROR)
            }

        }
    }

    fun setAnswerGetQuestion(rawJson: JsonObject) {
        mState.postValue(Cons.STATE_LOADING)
        val idUser = mSpHelp.getString(Cons.ID_USER)
        viewModelScope.launch {
            when(repositoryDiagnosis.setSymptomAnswer(interfaceApi, idUser, rawJson)){
                is ResultWrapper.Success -> getNextQuestion()
                is ResultWrapper.Error -> mState.postValue(Cons.STATE_ERROR)
            }
        }
    }

    fun saveHistory(rawJson: JsonObject) {
        mState.postValue(Cons.STATE_LOADING)
        viewModelScope.launch {
            when(val result = repositoryDiagnosis.saveHistory(interfaceApiCloud,rawJson)){
                is ResultWrapper.Success -> {
                    mState.postValue(Cons.STATE_SAVE_HISTORY_SUCCESS)
                    Log.d("Respon Save History", result.value)
                }
                //service under development
                is ResultWrapper.Error -> mState.postValue(Cons.STATE_SAVE_HISTORY_SUCCESS)
            }

        }
    }
}