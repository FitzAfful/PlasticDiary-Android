package com.glivion.plasticdiary.view.fragment.profile

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.HowItWorksLayoutBinding
import com.glivion.plasticdiary.databinding.ProfileLeaderboardFragmentBinding
import com.glivion.plasticdiary.model.profile.LeaderboardInterface
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.view.adapter.profile.LeaderboardAdapter
import com.glivion.plasticdiary.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LeaderboardFragment : Fragment() {
    private lateinit var binding: ProfileLeaderboardFragmentBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var thisWeekLeaderboardAdapter: LeaderboardAdapter
    private lateinit var allTimeLeaderboardAdapter: LeaderboardAdapter
    private val thisWeekList = ArrayList<LeaderboardInterface>()
    private val allTimeList = ArrayList<LeaderboardInterface>()
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_leaderboard_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {
        viewModel.getLeaderboard()

        viewModel.userLoader.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                binding.swipeRefresh.isRefreshing = it
            }
        })

        viewModel.userErrors.observe(viewLifecycleOwner, { isError ->
            isError.let { e ->
                Timber.e("initViewModel: ${e.localizedMessage}")
                binding.swipeRefresh.isRefreshing = false
                showErrorMessage(binding.parentLayout, e)
            }
        })

        viewModel.leaderboard.observe(viewLifecycleOwner, {
            allTimeList.clear()
            thisWeekList.clear()
            allTimeList.addAll(it.leaderboard?.allTime!!)
            thisWeekList.addAll(it.leaderboard?.thisWeek!!)
            thisWeekLeaderboardAdapter.submitList(thisWeekList)
            allTimeLeaderboardAdapter.submitList(allTimeList)
        })
    }

    private fun initViews() {
        navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        thisWeekLeaderboardAdapter = LeaderboardAdapter()
        allTimeLeaderboardAdapter = LeaderboardAdapter()
        binding.apply {
            backBtn.setOnClickListener {
                navController.navigateUp(appBarConfiguration)
            }
            learnMore.setOnClickListener {
                openLearnMoreDialog()
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.getLeaderboard()
            }
            allTimeRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = allTimeLeaderboardAdapter
            }
            thisWeekRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = thisWeekLeaderboardAdapter
            }

            thisWeek.setOnClickListener {
                thisWeek.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                allTime.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.card_bg_grey
                    )
                )
                allTimeRecyclerview.visibility = View.GONE
                thisWeekRecyclerview.visibility = View.VISIBLE
            }
            allTime.setOnClickListener {
                thisWeek.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.card_bg_grey
                    )
                )
                allTime.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                allTimeRecyclerview.visibility = View.VISIBLE
                thisWeekRecyclerview.visibility = View.GONE
            }
        }
    }

    private fun openLearnMoreDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.myFullscreenAlertDialogStyle)
        val binding:HowItWorksLayoutBinding  = DataBindingUtil.inflate(layoutInflater, R.layout.how_it_works_layout, null, false)

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        binding.apply {
            close.setOnClickListener {
                dialog.dismiss()
            }

        }

        dialog.show()
    }
}