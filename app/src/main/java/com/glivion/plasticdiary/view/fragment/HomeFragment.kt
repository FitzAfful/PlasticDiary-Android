package com.glivion.plasticdiary.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.HomeFragmentBinding
import com.glivion.plasticdiary.databinding.MyUsageLayoutBinding
import com.glivion.plasticdiary.model.HomeObject
import com.glivion.plasticdiary.model.home.Usage
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.util.showSnackBarMessage
import com.glivion.plasticdiary.view.adapter.home.HomePageAdapter
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var homePageAdapter: HomePageAdapter
    private var homeArrayList = ArrayList<HomeObject>()
    private val featuredList = ArrayList<Any>()
    private val newsList = ArrayList<Any>()
    private val videoList = ArrayList<Any>()
    private val researchList = ArrayList<Any>()
    private val articleList = ArrayList<Any>()
    private val usageList = ArrayList<Usage>()
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

        viewModel.data.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = false
            homeArrayList.clear()
            usageList.clear()
            usageList.addAll(it.usage!!)
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
            articleList.addAll(it.article!!)
            homeArrayList.add(HomeObject("Articles", "See more articles", "articles", articleList))
            // research
            researchList.clear()
            researchList.addAll(it.research!!)
            //homeArrayList.add(HomeObject("Research", null, "research", researchList))


            homePageAdapter.setUpHomPageItems(homeArrayList)
            Timber.e("home: $homeArrayList")

        })
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(requireContext())

        homePageAdapter = HomePageAdapter(requireContext(), ArrayList())

        binding.apply {
            swipeRefresh.setOnRefreshListener {
                viewModel.getHomePageItems()
            }
            recordUsage.setOnClickListener {
                openUsageDialog()
            }
            homeRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = homePageAdapter
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}