package com.glivion.plasticdiary.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.data.callbacks.HomePageCallback
import com.glivion.plasticdiary.databinding.HomeFragmentBinding
import com.glivion.plasticdiary.databinding.MyUsageLayoutBinding
import com.glivion.plasticdiary.databinding.UsageResponseLayoutBinding
import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.HomeObject
import com.glivion.plasticdiary.model.home.*
import com.glivion.plasticdiary.util.*
import com.glivion.plasticdiary.view.adapter.home.HomePageAdapter
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.view.dialog.StreakBottomSheetDialog
import com.glivion.plasticdiary.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment(), HomePageCallback {
    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var homePageAdapter: HomePageAdapter
    private var homeArrayList = ArrayList<HomeObject>()
    private val featuredList = ArrayList<Any>()
    private val newsList = ArrayList<Any>()
    private val videoList = ArrayList<Any>()
    private val researchList = ArrayList<Any>()
    private val researchList_3 = ArrayList<Any>()
    private val articleList = ArrayList<Any>()
    private val articleList_3 = ArrayList<Any>()
    private val usageList = ArrayList<Usage>()
    private val lastFiveUsagesList = ArrayList<Usage>()
    private val entriesList = ArrayList<BarEntry>()
    var currentStreak = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        initViews()
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {
        viewModel.apply {
            getCurrentStreak()
            submitStreak("1")
            getHomePageItems()
        }
        viewModel.userLoader.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                binding.swipeRefresh.isRefreshing = it
            }
        })

        viewModel.loadingDialog.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                if (it) {
                    loadingDialog.startProgressDialog()
                } else {
                    loadingDialog.dismissDialog()
                }
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


        viewModel.responses.observe(viewLifecycleOwner, { response ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.swipeRefresh.isRefreshing = false
                Timber.e("response: $response")
                showSnackBarMessage(binding.parentLayout, response)
            }
        })

        viewModel.streak.observe(viewLifecycleOwner, {
            currentStreak = it
            binding.streaksTxt.text = it.toString()
        })

        viewModel.category.observe(viewLifecycleOwner, {
           showUsageSuccessDialog(it)
        })

        viewModel.data.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = false

            if (it.usage!!.isNotEmpty()) {
                binding.usageLyt.visibility = View.VISIBLE
            }
            homeArrayList.clear()
            lastFiveUsagesList.clear()
            lastFiveUsagesList.addAll(it.usage!!.takeLast(5))
            // add featured
            featuredList.clear()
            featuredList.addAll(it.featuredNews!!)
            homeArrayList.add(HomeObject("Top News", null, "featured_news", featuredList))
            // add news
            newsList.clear()
            newsList.addAll(it.news!!)
            homeArrayList.add(HomeObject(null, "See more news", "news", newsList))
            // videos
            videoList.clear()
            videoList.addAll(it.video!!)
            homeArrayList.add(HomeObject("Videos", null, "videos", videoList))
            // articles
            articleList.clear()
            articleList_3.clear()
            articleList.addAll(it.article!!)
            articleList_3.addAll(it.article!!.shuffled().take(3))
            homeArrayList.add(HomeObject("Articles", "See more articles", "articles", articleList_3))
            // research
            researchList.clear()
            researchList_3.clear()
            researchList.addAll(it.research!!)
            researchList_3.addAll(it.research!!.shuffled().take(3))
            homeArrayList.add(HomeObject("Research", "See more", "research", researchList_3))
            homePageAdapter.setUpHomPageItems(homeArrayList)

            for ((index, usage) in lastFiveUsagesList.withIndex()) {
                entriesList.add(BarEntry(index.toFloat(), usage.amount!!.toFloat()))
            }

            Timber.e("$entriesList")
            val dataSet = BarDataSet(entriesList, "Usage History")
            dataSet.apply {
                colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
                valueTextColor = ContextCompat.getColor(requireContext(), R.color.text_black)
                valueTextSize = 15f
            }
            val barData = BarData(dataSet)
            barData.apply {
                setValueTextSize(12f)
                barWidth = 0.9f
            }
            binding.usageBarChat.apply {
                data = barData
                setFitBars(true)
                animateY(1000)
                setDrawGridBackground(false)
                description.isEnabled = false
                notifyDataSetChanged()
                invalidate()
            }

        })
    }


    private fun initViews() {
        loadingDialog = LoadingDialog(requireContext())


        val xAxis = binding.usageBarChat.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            textColor = ContextCompat.getColor(requireContext(), R.color.text_black)
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawGridLinesBehindData(false)

            valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val x = value.toInt()
                    return if (x >= 0 && x <= lastFiveUsagesList.size) {
                        Timber.e("x: $x $lastFiveUsagesList")
                        getDayAndMonth(lastFiveUsagesList[x].date.toString())
                    } else {
                        ""
                    }
                }
            }
        }
        val yAxisRight = binding.usageBarChat.axisRight
        val yAxisLeft = binding.usageBarChat.axisLeft
        yAxisRight.apply {
            textColor = ContextCompat.getColor(requireContext(), R.color.text_black)
        }
        yAxisLeft.apply {
            textColor = ContextCompat.getColor(requireContext(), R.color.text_black)
        }
        val legend = binding.usageBarChat.legend
        legend.apply {
            textColor = ContextCompat.getColor(requireContext(), R.color.text_black)
            textSize = 11f
            formSize = 9f
        }
        homePageAdapter =  HomePageAdapter(requireContext(), ArrayList(), this)

        binding.apply {
            swipeRefresh.setOnRefreshListener {
                viewModel.getHomePageItems()
            }
            recordUsage.setOnClickListener {
                openUsageDialog()
            }
            homeRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = homePageAdapter
            }

            streaksIcon.setOnClickListener {
                val dialog = StreakBottomSheetDialog(requireContext(), currentStreak)
                dialog.showNow(childFragmentManager, "StreakBottomSheetDialog")
            }
        }
    }

    private fun openUsageDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.myFullscreenAlertDialogStyle)
        val binding: MyUsageLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.my_usage_layout, null, false)

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        binding.apply {
            cancel.setOnClickListener {
                dialog.dismiss()
            }

            saveBtn.setOnClickListener {
                val number = usageInputField.text.toString()
                if (number.isEmpty()) {
                    usageInputField.error = "Please enter number"
                    return@setOnClickListener
                }
                dialog.dismiss()
                viewModel.submitUsage(number)
            }
        }

        dialog.show()
    }

    private fun showUsageSuccessDialog(response: BaseAuthResponse?) {
        val builder = AlertDialog.Builder(requireActivity())
        val binding: UsageResponseLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.usage_response_layout, null, false)

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        binding.apply {
            close.setOnClickListener {
                dialog.dismiss()
            }
            usageSuccessTxt.text = response?.message
        }

        dialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun seeMoreArticles() {
        val bundle = Bundle().apply {
            putParcelableArrayList(WEB_VIEW_ARTICLE_LIST, articleList as ArrayList<Article>)
        }
        viewMoreItems(requireContext(), "Articles", bundle)
    }

    override fun seeMoreNews() {
        val bundle = Bundle().apply {
            putParcelableArrayList(WEB_VIEW_NEWS_LIST, newsList as ArrayList<News>)
        }
        viewMoreItems(requireContext(), "News", bundle)
    }

    override fun seeMoreResearch() {
        val bundle = Bundle().apply {
            putParcelableArrayList(WEB_VIEW_NEWS_LIST, researchList as ArrayList<Research>)
        }
        viewMoreItems(requireContext(), "Research", bundle)
    }

    override fun bookmark(id: Int, type: String) {
        viewModel.bookmark(id, type)
    }


}