package com.glivion.plasticdiary.data.database.dao.explore

import androidx.room.Dao
import androidx.room.Query
import com.glivion.plasticdiary.data.database.dao.BaseDao
import com.glivion.plasticdiary.model.explore.Tip
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface TipsDao: BaseDao<Tip> {

    @Query("select * from tips")
    fun getAllTips(): Flowable<List<Tip>>

    @Query("select * from tips where id = :id")
    fun getASingleTip(id: Int): Single<Tip>
}