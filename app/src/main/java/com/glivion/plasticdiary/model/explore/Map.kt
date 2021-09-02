package com.glivion.plasticdiary.model.explore

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "maps")
data class Map(
    @PrimaryKey
    var id: Int? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var name: String? = null
) : Parcelable
