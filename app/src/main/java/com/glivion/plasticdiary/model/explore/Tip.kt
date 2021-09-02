package com.glivion.plasticdiary.model.explore

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tips")
data class Tip(
    var content: String? = null,
    @PrimaryKey
    var id: Int? = null
) : Parcelable
