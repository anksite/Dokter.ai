package com.dokter.ai.di

import android.content.Context
import androidx.room.Room
import com.dokter.ai.data.local.room.AppDatabase
import com.dokter.ai.data.local.room.DaoHistory
import com.dokter.ai.util.Cons
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Cons.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Provides
    fun provideDaoHistory(database: AppDatabase): DaoHistory = database.daoHistory()
}