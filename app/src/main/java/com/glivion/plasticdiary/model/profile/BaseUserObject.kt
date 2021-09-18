package com.glivion.plasticdiary.model.profile


import com.glivion.plasticdiary.model.home.BaseHomeResponse
import com.google.gson.annotations.SerializedName

data class BaseUserObject(
    var bookmarks: BaseHomeResponse? = null,
    var details: Details? = null
) {
    data class Details(
        var id: Int? = null,
        @SerializedName("img_url")
        var imgUrl: String? = null,
        var name: String? = null,
        var platform: String? = null,
        var token: String? = null
    )
}