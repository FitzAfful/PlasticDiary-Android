package com.glivion.plasticdiary.data.database.dao.explore

import androidx.room.Dao
import androidx.room.Query
import com.glivion.plasticdiary.data.database.dao.BaseDao
import com.glivion.plasticdiary.model.explore.Map
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface MapsDao: BaseDao<Map> {
    @Query("select * from maps")
    fun getAllMapItems(): Flowable<List<Map>>

    @Query("select * from maps where id = :id")
    fun getASingleMapItem(id: Int): Single<Map>
}