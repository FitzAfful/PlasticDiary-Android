package com.glivion.plasticdiary.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Research(
    var abstract: String? = null,
    var author: String? = null,
    var doi: String? = null,
    var id: Int? = null,
    var link: String? = null,
    var title: String? = null,
    var year: Int? = null
) : Parcelable
