package com.glivion.plasticdiary.data.database.dao.explore

import androidx.room.Dao
import androidx.room.Query
import com.glivion.plasticdiary.data.database.dao.BaseDao
import com.glivion.plasticdiary.model.explore.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface ResourcesDao: BaseDao<Resource> {
    @Query("select * from resources")
    fun getAllResources(): Flowable<List<Resource>>

    @Query("select * from resources where id = :id")
    fun getASingleResource(id: Int): Single<Resource>
}