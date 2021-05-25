package com.dokter.ai.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dokter.ai.data.DataMapper
import com.dokter.ai.data.DataSymptom
import com.dokter.ai.data.network.InterfaceApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMChooseSymptom @Inject constructor(): ViewModel() {
    @Inject lateinit var interfaceApi: InterfaceApi

    val mListSymptom = MutableLiveData<List<DataSymptom>>()
    val listSymptom : LiveData<List<DataSymptom>> = mListSymptom

    val ioScope = CoroutineScope(Dispatchers.IO)

    fun getSymptom() {
        ioScope.launch {
            mListSymptom.postValue(DataMapper.mapResponseToDomain(interfaceApi.symptom()))
        }
    }
}