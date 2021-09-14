package com.glivion.plasticdiary.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var id: Int? = null,
    var image: String? = null,
    var title: String? = null,
    var url: String? = null
) : Parcelable
