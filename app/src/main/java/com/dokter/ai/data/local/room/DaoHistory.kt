package com.dokter.ai.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dokter.ai.data.local.entity.EntityHistory

@Dao
interface DaoHistory {
    @Insert
    suspend fun insertHistory(data: EntityHistory)

    @Query("SELECT * FROM EntityHistory")
    fun selectListHistory(): LiveData<List<EntityHistory>>
}