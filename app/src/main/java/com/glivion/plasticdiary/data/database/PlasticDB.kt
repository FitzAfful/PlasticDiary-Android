package com.glivion.plasticdiary.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.glivion.plasticdiary.data.database.dao.explore.MapsDao
import com.glivion.plasticdiary.data.database.dao.explore.ResourcesDao
import com.glivion.plasticdiary.data.database.dao.explore.TipsDao
import com.glivion.plasticdiary.model.explore.Map
import com.glivion.plasticdiary.model.explore.Resource
import com.glivion.plasticdiary.model.explore.Tip
import com.glivion.plasticdiary.util.DATABASE_NAME

@Database(entities = [Tip::class, Resource::class, Map::class], version = 1, exportSchema = false)
abstract class PlasticDB: RoomDatabase() {
    // explore
    abstract fun tipsDao(): TipsDao
    abstract fun resourceDao(): ResourcesDao
    abstract fun mapsDao(): MapsDao

    companion object {
        @Volatile
        private var INSTANCE: PlasticDB? = null

        private var LOCK = Any()

        fun getInstance(application: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(application).also { INSTANCE = it }
        }
        private fun buildDatabase(application: Context) = Room.databaseBuilder(application, PlasticDB::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}