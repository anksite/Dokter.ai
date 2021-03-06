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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMChooseSymptom @Inject constructor(val repositoryDiagnosis: RepositoryDiagnosis): ViewModel() {
    @Inject lateinit var interfaceApi: InterfaceApi
    @Inject lateinit var mSpHelp: SpHelp

    val mListSymptom = MutableLiveData<List<DataSymptom>>()
    val listSymptom : LiveData<List<DataSymptom>> = mListSymptom

    val mPrepareState = MutableLiveData<String>()
    val prepareState : LiveData<String> = mPrepareState

    val ioScope = CoroutineScope(Dispatchers.IO)

    fun getSymptom() {
        ioScope.launch {
            when(val result = repositoryDiagnosis.getAllSymptom(interfaceApi)){
                is ResultWrapper.Success -> mListSymptom.postValue(result.value)
                is ResultWrapper.Error -> mPrepareState.postValue(Cons.STATE_ERROR)
            }
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