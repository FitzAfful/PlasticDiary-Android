package com.glivion.plasticdiary.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class HomeObject(
    var title: String? = null,
    var footer: String? = null,
    var type: String? = null,
    var data: ArrayList<Any>? = null
)