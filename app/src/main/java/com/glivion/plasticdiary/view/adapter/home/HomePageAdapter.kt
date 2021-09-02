package com.glivion.plasticdiary.view.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.HomeItemLayoutBinding
import com.glivion.plasticdiary.model.HomeObject

class HomePageAdapter(val context: Context, data: ArrayList<HomeObject>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val FEATURED_NEWS = 1
    private val NEWS = 2
    private val VIDEOS = 3
    private val ARTICLES = 4
    private val NO_DATA_VIEW = -1

    init {
        homePageItems = data
    }

    companion object {
        private var homePageItems = ArrayList<HomeObject>()
    }

    class FeaturedNewsViewHolder(private val binding: HomeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(homeObject: HomeObject, context: Context, homeAdapter: HomePageAdapter) {
            if (homeObject.title.isNullOrEmpty()) {
                binding.homeHeader.visibility = View.GONE
            }
            if (homeObject.footer.isNullOrEmpty()) {
                binding.homeFooter.visibility = View.GONE
            }
            //val childAdapter = HomePageChildAdapter(context, homeObject.data )
            binding.apply {
                homeHeader.text = homeObject.title
                homeFooter.text = homeObject.footer
                homeItemRecyclerview.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    //adapter = childAdapter
                }
            }
        }
    }

    class NewsViewHolder(private val binding: HomeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(homeObject: HomeObject, context: Context, homeAdapter: HomePageAdapter) {
            if (homeObject.title.isNullOrEmpty()) {
                binding.homeHeader.visibility = View.GONE
            }
            if (homeObject.footer.isNullOrEmpty()) {
                binding.homeFooter.visibility = View.GONE
            }
            //val childAdapter = HomePageChildAdapter(context, homeObject.data )
            binding.apply {
                homeHeader.text = homeObject.title
                homeFooter.text = homeObject.footer
                homeItemRecyclerview.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    //adapter = childAdapter
                }
            }
        }
    }

    class VideosViewHolder(private val binding: HomeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(homeObject: HomeObject, context: Context, homeAdapter: HomePageAdapter) {
            if (homeObject.title.isNullOrEmpty()) {
                binding.homeHeader.visibility = View.GONE
            }
            if (homeObject.footer.isNullOrEmpty()) {
                binding.homeFooter.visibility = View.GONE
            }
            //val childAdapter = HomePageChildAdapter(context, homeObject.data )
            binding.apply {
                homeHeader.text = homeObject.title
                homeFooter.text = homeObject.footer
                homeItemRecyclerview.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    //adapter = childAdapter
                }
            }
        }
    }

    class ArticlesViewHolder(private val binding: HomeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(homeObject: HomeObject, context: Context, homeAdapter: HomePageAdapter) {
            if (homeObject.title.isNullOrEmpty()) {
                binding.homeHeader.visibility = View.GONE
            }
            if (homeObject.footer.isNullOrEmpty()) {
                binding.homeFooter.visibility = View.GONE
            }
            //val childAdapter = HomePageChildAdapter(context, homeObject.data )
            binding.apply {
                homeHeader.text = homeObject.title
                homeFooter.text = homeObject.footer
                homeItemRecyclerview.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    //adapter = childAdapter
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val binding: HomeItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.home_item_layout,
            parent,
            false
        )
        when (viewType) {
            FEATURED_NEWS -> {
              viewHolder =  FeaturedNewsViewHolder(binding)
            }
            NEWS -> {
                viewHolder =  NewsViewHolder(binding)
            }
            VIDEOS -> {
                viewHolder =  VideosViewHolder(binding)
            }
            ARTICLES -> {
                viewHolder =  ArticlesViewHolder(binding)
            }
       }
        return viewHolder

    }

    override fun getItemCount(): Int = homePageItems.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int {
        val homeObject = homePageItems[position]
        return when (homeObject.type) {
            "featured_news" -> {
                FEATURED_NEWS
            }
            "news" -> {
                NEWS
            }
            "videos" -> {
                VIDEOS
            }
            "articles" -> {
                ARTICLES
            }
            else -> NO_DATA_VIEW
        }
    }

    fun setUpHomPageItems(items: ArrayList<HomeObject>) {
        homePageItems = items
        notifyDataSetChanged()
        //notifyItemRangeChanged(0, homePageItems.size)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val homeItem = homePageItems[position]
        when (holder) {
            is FeaturedNewsViewHolder -> {
                holder.bind(homeItem, context, this)
            }
            is NewsViewHolder -> {
                holder.bind(homeItem, context,this)
            }
            is VideosViewHolder -> {
                holder.bind(homeItem, context,this)
            }
            is ArticlesViewHolder -> {
                holder.bind(homeItem, context,this)
            }
        }
    }
}