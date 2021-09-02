package com.glivion.plasticdiary.model.home


import com.google.gson.annotations.SerializedName

data class BaseHomeResponse(
    var article: List<Article>? = null,
    @SerializedName("featured_news")
    var featuredNews: List<FeaturedNews>? = null,
    var news: List<News>? = null,
    var research: List<Research>? = null,
    var usage: List<Usage>? = null,
    var video: List<Video>? = null
)