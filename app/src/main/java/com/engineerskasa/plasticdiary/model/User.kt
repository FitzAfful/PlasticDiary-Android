package com.engineerskasa.plasticdiary.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("created_at")
    var createdAt: String? = null,
    var id: Int? = null,
    @SerializedName("img_url")
    var imgUrl: String? = null,
    var name: String? = null,
    var platform: String? = null,
    var token: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null
)
