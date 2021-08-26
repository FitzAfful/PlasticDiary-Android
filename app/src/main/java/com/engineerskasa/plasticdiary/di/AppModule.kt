package com.engineerskasa.plasticdiary.di

import android.content.Context
import com.engineerskasa.plasticdiary.preference.AppPreference
import com.engineerskasa.plasticdiary.util.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideAppPreference(@ApplicationContext application: Context): AppPreference = AppPreference(application)

    @Provides
    @Singleton
    fun provideUtils(@ApplicationContext application: Context): Utils = Utils(application)
}