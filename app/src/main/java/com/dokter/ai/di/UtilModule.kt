package com.dokter.ai.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.dokter.ai.data.RepositoryDiagnosis
import com.dokter.ai.util.SpHelp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilModule {

    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext context: Context) : RequestManager = Glide.with(context)

    @Singleton
    @Provides
    fun provideSpHelp(@ApplicationContext context: Context) : SpHelp = SpHelp(context)

    @Singleton
    @Provides
    fun provideRepositoryDiagnosis() : RepositoryDiagnosis = RepositoryDiagnosis()
}