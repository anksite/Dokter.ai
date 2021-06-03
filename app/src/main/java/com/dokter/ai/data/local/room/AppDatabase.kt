package com.dokter.ai.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dokter.ai.data.local.entity.EntityHistory
import com.dokter.ai.data.local.room.DaoHistory

@Database(entities = [EntityHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun daoHistory(): DaoHistory
}