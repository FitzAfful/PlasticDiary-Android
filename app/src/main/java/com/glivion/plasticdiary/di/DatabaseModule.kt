package com.glivion.plasticdiary.di

import android.content.Context
import com.glivion.plasticdiary.data.database.PlasticDB
import com.glivion.plasticdiary.data.database.dao.explore.MapsDao
import com.glivion.plasticdiary.data.database.dao.explore.ResourcesDao
import com.glivion.plasticdiary.data.database.dao.explore.TipsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun providePlasticDB(@ApplicationContext application: Context): PlasticDB = PlasticDB.getInstance(application)

    @Singleton
    @Provides
    fun provideTipDao(database: PlasticDB): TipsDao = database.tipsDao()

    @Singleton
    @Provides
    fun provideResourcesDao(database: PlasticDB): ResourcesDao = database.resourceDao()

    @Singleton
    @Provides
    fun provideMapsDao(database: PlasticDB): MapsDao = database.mapsDao()

}