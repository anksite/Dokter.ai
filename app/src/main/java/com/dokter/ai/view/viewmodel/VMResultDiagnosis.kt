package com.dokter.ai.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokter.ai.data.RepositoryDiagnosis
import com.dokter.ai.data.local.entity.EntityHistory
import com.dokter.ai.data.local.room.DaoHistory
import com.dokter.ai.data.network.InterfaceApi
import com.dokter.ai.data.network.ResponseDisease
import com.dokter.ai.data.network.ResultWrapper
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMResultDiagnosis @Inject constructor(val repositoryDiagnosis: RepositoryDiagnosis): ViewModel() {
    @Inject lateinit var interfaceApi: InterfaceApi
    @Inject lateinit var mSpHelp: SpHelp
    @Inject lateinit var daoHistory: DaoHistory

    val mDisease = MutableLiveData<String>()
    val disease : LiveData<String> = mDisease

    val mState = MutableLiveData<String>()
    val state : LiveData<String> = mState

    val ioScope = CoroutineScope(Dispatchers.IO)

    fun getResultDisease(idDisease: String) {
        mState.postValue(Cons.STATE_LOADING)
        ioScope.launch {
            when(val result = repositoryDiagnosis.getResultDisease(interfaceApi,idDisease)){
                is ResultWrapper.Success -> {
                    mState.postValue(Cons.STATE_SUCCESS)
                    mDisease.postValue(result.value)
                }
                is ResultWrapper.Error -> mState.postValue(Cons.STATE_ERROR)
            }

        }
    }

    fun saveHistoryLocal(entityHistory: EntityHistory){
        viewModelScope.launch {
            repositoryDiagnosis.saveHistoryLocal(daoHistory, entityHistory)
        }
    }
}