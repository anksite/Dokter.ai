package com.dokter.ai.view.viewmodel

import androidx.lifecycle.ViewModel
import com.dokter.ai.data.RepositoryDiagnosis
import com.dokter.ai.data.local.room.DaoHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VMHistory @Inject constructor(val repositoryDiagnosis: RepositoryDiagnosis) : ViewModel() {
    @Inject
    lateinit var daoHistory: DaoHistory

    fun getHistory() = repositoryDiagnosis.selectHistoryLocal(daoHistory)
}