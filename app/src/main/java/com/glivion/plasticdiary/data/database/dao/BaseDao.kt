package com.glivion.plasticdiary.data.database.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Completable

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T)

    @Update
    fun update(data: T)

    @Delete
    fun delete(data: T)

    @Delete
    fun delete(data: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<T>): Completable
}