package com.glivion.plasticdiary.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.data.callbacks.HomePageCallback
import com.glivion.plasticdiary.databinding.ProfileBookmarksFragmentBinding
import com.glivion.plasticdiary.model.HomeObject
import com.glivion.plasticdiary.model.home.*
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.util.showInWebView
import com.glivion.plasticdiary.view.adapter.misc.BookMarksAdapter
import com.glivion.plasticdiary.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BookmarksFragment : Fragment(), HomePageCallback {
    private lateinit var binding: ProfileBookmarksFragmentBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bookMarksAdapter: BookMarksAdapter
    private val viewModel: ProfileViewModel by viewModels()
    private var homeArrayList = ArrayList<HomeObject>()
    private val featuredList = ArrayList<Any>()
    private val newsList = ArrayList<Any>()
    private val videoList = ArrayList<Any>()
    private val researchList = ArrayList<Any>()
    private val articleList = ArrayList<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.profile_bookmarks_fragment,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {

        viewModel.getUserProfileAndBookmarks()
        viewModel.userLoader.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                binding.swipeRefresh.isRefreshing = it
            }
        })
        viewModel.userErrors.observe(viewLifecycleOwner, { isError ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                isError.let { e ->
                    Timber.e("initViewModel: ${e.localizedMessage}")
                    binding.swipeRefresh.isRefreshing = false
                    showErrorMessage(binding.parentLayout, e)
                }
            }
        })
        viewModel.bookmarks.observe(viewLifecycleOwner, {
            homeArrayList.clear()
            // add news
            newsList.clear()
            it.news?.let { it1 -> newsList.addAll(it1) }
            homeArrayList.add(HomeObject("News", null, "news", newsList))
            // videos
            videoList.clear()
            it.video?.let { it1 -> videoList.addAll(it1) }
            homeArrayList.add(HomeObject("Videos", null, "videos", videoList))
            // articles
            articleList.clear()
            it.article?.let { it1 -> articleList.addAll(it1) }
            homeArrayList.add(HomeObject("Articles", null, "articles", articleList))
            // research
            researchList.clear()
            it.research?.let { it1 -> researchList.addAll(it1) }
            homeArrayList.add(HomeObject("Research", null, "research", researchList))
            bookMarksAdapter.setUpItems(homeArrayList)
        })

    }

    private fun initViews() {
        bookMarksAdapter = BookMarksAdapter(requireContext(), ArrayList(), this)
        navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                navController.navigateUp(appBarConfiguration)
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.getUserProfileAndBookmarks()
            }
            bookmarksRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = bookMarksAdapter
            }
        }
    }

    override fun onSelectFeaturedNews(news: FeaturedNews) {
        showInWebView(requireContext(), news.url, news.id, "news")
    }

    override fun onSelectNewsItem(news: News) {
        showInWebView(requireContext(), news.url, news.id, "news")
    }

    override fun onSelectVideos(video: Video) {
        showInWebView(requireContext(), video.link, video.id, "video")
    }

    override fun onSelectArticles(article: Article) {
        showInWebView(requireContext(), article.url, article.id, "article")
    }

    override fun onSelectResearchItem(research: Research) {
        showInWebView(requireContext(), research.link, research.id, "research")
    }

}