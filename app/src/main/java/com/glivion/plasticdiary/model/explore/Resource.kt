package com.glivion.plasticdiary.model.explore

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "resources")
data class Resource(
    var description: String? = null,
    @PrimaryKey
    var id: Int? = null,
    var image: String? = null,
    var link: String? = null,
    var title: String? = null
) : Parcelable
