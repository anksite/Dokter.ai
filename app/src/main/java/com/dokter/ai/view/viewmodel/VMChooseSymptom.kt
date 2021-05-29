package com.dokter.ai.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dokter.ai.data.DataMapper
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.data.RepositoryDiagnosis
import com.dokter.ai.data.network.InterfaceApi
import com.dokter.ai.data.network.ResultWrapper
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VMChooseSymptom constructor(val repositoryDiagnosis: RepositoryDiagnosis): ViewModel() {
    lateinit var interfaceApi: InterfaceApi
    lateinit var mSpHelp: SpHelp

    val mListSymptom = MutableLiveData<List<DataSymptom>>()
    val listSymptom : LiveData<List<DataSymptom>> = mListSymptom

    val mPrepareState = MutableLiveData<String>()
    val prepareState : LiveData<String> = mPrepareState

    val ioScope = CoroutineScope(Dispatchers.IO)

    fun getSymptom() {
        ioScope.launch {
            mListSymptom.postValue(DataMapper.mapResponseToDomain(interfaceApi.symptom()))
        }
    }

    fun prepareDiagnosis(idSymptom: String) {
        mPrepareState.postValue(Cons.STATE_LOADING)
        val idUser = mSpHelp.getString(Cons.ID_USER)
        ioScope.launch {
            when(repositoryDiagnosis.resetQuestionTree(interfaceApi, idUser)){
                is ResultWrapper.Success -> {
                    when(repositoryDiagnosis.setInitialSymptom(interfaceApi, idUser, idSymptom)){
                        is ResultWrapper.Success -> {
                            mPrepareState.postValue(Cons.STATE_SUCCESS)
                        }

                        is ResultWrapper.Error -> {
                            mPrepareState.postValue(Cons.STATE_ERROR)
                        }
                    }
                }

                is ResultWrapper.Error -> {
                    mPrepareState.postValue(Cons.STATE_ERROR)
                }
            }
        }

    }
}