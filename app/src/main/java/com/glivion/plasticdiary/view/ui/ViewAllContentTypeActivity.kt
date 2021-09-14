package com.glivion.plasticdiary.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.data.callbacks.HomePageCallback
import com.glivion.plasticdiary.databinding.ActivityViewAllContentTypeBinding
import com.glivion.plasticdiary.model.HomeObject
import com.glivion.plasticdiary.model.explore.Tip
import com.glivion.plasticdiary.model.home.*
import com.glivion.plasticdiary.util.*
import com.glivion.plasticdiary.view.adapter.misc.BookMarksAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllContentTypeActivity : AppCompatActivity(), HomePageCallback {
    private var contentType: String? = null
    private var articleList: ArrayList<Article>? = null
    private var newsList: ArrayList<News>? = null
    private var tipsList: ArrayList<Tip>? = null
    private lateinit var bookMarksAdapter: BookMarksAdapter
    private var homeArrayList = ArrayList<HomeObject>()
    private lateinit var binding: ActivityViewAllContentTypeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_content_type)
        binding.lifecycleOwner = this
        getIntentParams()
        initViews()
    }

    private fun initViews() {
        bookMarksAdapter = BookMarksAdapter(this, ArrayList(), this)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }
            toolbar.title = contentType
            viewMoreRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@ViewAllContentTypeActivity, LinearLayoutManager.VERTICAL, false)
                adapter = bookMarksAdapter
            }
        }
        bookMarksAdapter.setUpItems(homeArrayList)
    }

    private fun getIntentParams() {
        intent?.extras?.let {
            contentType = it.getString(WEB_VIEW_TYPE)
            articleList = it.getParcelableArrayList(WEB_VIEW_ARTICLE_LIST)
            newsList = it.getParcelableArrayList(WEB_VIEW_NEWS_LIST)
            tipsList = it.getParcelableArrayList(WEB_VIEW_TIPS_LIST)
        }
        homeArrayList.clear()
        homeArrayList.add(HomeObject(null, null, "articles", articleList as? ArrayList<Any>))
        homeArrayList.add(HomeObject(null, null, "news", newsList as? ArrayList<Any>))
        homeArrayList.add(HomeObject(null, null, "tips", tipsList as? ArrayList<Any>))
    }

    override fun onSelectFeaturedNews(news: FeaturedNews) {
        showInWebView(this, news.url, news.id, "news")
    }

    override fun onSelectNewsItem(news: News) {
        showInWebView(this, news.url, news.id, "news")
    }

    override fun onSelectVideos(video: Video) {
        showInWebView(this, video.link, video.id, "video")
    }

    override fun onSelectArticles(article: Article) {
        showInWebView(this, article.url, article.id, "article")
    }

    override fun onSelectResearchItem(research: Research) {
        showInWebView(this, research.link, research.id, "research")
    }

    override fun onSelectTipItem(tip: Tip) {
        openViewMoreInfoDialog(this, "Tip", tip.content!!)
    }

    override fun seeMoreArticles() {

    }

    override fun seeMoreNews() {

    }

    override fun bookmark(id: Int, type: String) {

    }
}