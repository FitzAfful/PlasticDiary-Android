package com.glivion.plasticdiary.view.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.*
import com.glivion.plasticdiary.model.home.*

class HomePageChildAdapter(val context: Context, val childItems: ArrayList<Any>?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val FEATURED_NEWS = 1
    private val NEWS = 2
    private val VIDEOS = 3
    private val ARTICLES = 4
    private val RESEARCH = 5
    private val NO_DATA_VIEW = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            FEATURED_NEWS -> {
                val binding: TopNewsHeaderLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.top_news_header_layout,
                    parent,
                    false
                )
                viewHolder = FeaturedNewsViewHolder(binding)
            }
            NEWS -> {
                val binding: TopNewsItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.top_news_item_layout,
                    parent,
                    false
                )
                viewHolder = NewsViewHolder(binding)
            }
            VIDEOS -> {
                val binding: VideoItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.video_item_layout,
                    parent,
                    false
                )
                viewHolder = VideosViewHolder(binding)
            }
            ARTICLES -> {
                val binding: ArticleItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.article_item_layout,
                    parent,
                    false
                )
                viewHolder = ArticlesViewHolder(binding)
            }
            RESEARCH -> {
                val binding: ResearchItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.research_item_layout,
                    parent,
                    false
                )
                viewHolder = ResearchViewHolder(binding)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FeaturedNewsViewHolder -> {
                val featuredNews = childItems?.get(position) as FeaturedNews
                holder.bind(featuredNews, context, this)
            }
            is NewsViewHolder -> {
                val news = childItems?.get(position) as News
                holder.bind(news, context, this)
            }
            is VideosViewHolder -> {
                val videos = childItems?.get(position) as Video
                holder.bind(videos, context, this)
            }
            is ArticlesViewHolder -> {
                val articles = childItems?.get(position) as Article
                holder.bind(articles, context, this)
            }
            is ResearchViewHolder -> {
                val research = childItems?.get(position) as Research
                holder.bind(research, context, this)
            }
        }
    }

    override fun getItemCount(): Int = childItems!!.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int {
        val homeObject = childItems?.get(position)
        return when (homeObject) {
            is FeaturedNews -> {
                FEATURED_NEWS
            }
            is News -> {
                NEWS
            }
            is Video -> {
                VIDEOS
            }
           is Article -> {
                ARTICLES
            }
            is Research -> {
                RESEARCH
            }
            else -> NO_DATA_VIEW
        }
    }

    class FeaturedNewsViewHolder(private val binding: TopNewsHeaderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: FeaturedNews, context: Context, childAdapter: HomePageChildAdapter) {
            binding.apply {
                featured = news
            }
        }
    }

    class NewsViewHolder(private val binding: TopNewsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_news: News, context: Context, childAdapter: HomePageChildAdapter) {
            binding.apply {
                news = _news
            }
        }
    }

    class VideosViewHolder(private val binding: VideoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_video: Video, childAdapter: Context, homePageChildAdapter: HomePageChildAdapter) {
            binding.apply {
                video = _video
            }
        }
    }

    class ArticlesViewHolder(private val binding: ArticleItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            _article: Article,
            childAdapter: Context,
            homePageChildAdapter: HomePageChildAdapter
        ) {
            binding.apply {
                article = _article
            }
        }
    }
    class ResearchViewHolder(private val binding: ResearchItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            _research: Research,
            childAdapter: Context,
            homePageChildAdapter: HomePageChildAdapter
        ) {
            binding.apply {
                research = _research
            }
        }
    }
}