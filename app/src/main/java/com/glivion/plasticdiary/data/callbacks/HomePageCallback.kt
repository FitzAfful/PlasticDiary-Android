package com.glivion.plasticdiary.data.callbacks

import com.glivion.plasticdiary.model.home.*

interface HomePageCallback {
    fun onSelectFeaturedNews(news: FeaturedNews)
    fun onSelectNewsItem(news: News)
    fun onSelectVideos(video: Video)
    fun onSelectArticles(article: Article)
    fun onSelectResearchItem(research: Research)
}