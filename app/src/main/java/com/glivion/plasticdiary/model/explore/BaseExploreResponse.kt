package com.glivion.plasticdiary.model.explore


import com.google.gson.annotations.SerializedName

data class BaseExploreResponse(
    @SerializedName("map_item")
    var mapItem: List<Map>? = null,
    var resource: List<Resource>? = null,
    var tips: List<Tip>? = null
)