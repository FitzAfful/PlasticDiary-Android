package com.glivion.plasticdiary.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ProfileFeedbackLayoutBinding
import com.glivion.plasticdiary.databinding.ProfileFragmentBinding
import com.glivion.plasticdiary.model.profile.Badges
import com.glivion.plasticdiary.util.REWARD_TITLE
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.util.showSnackBarMessage
import com.glivion.plasticdiary.view.adapter.profile.BadgesAdapter
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.viewmodel.AuthViewModel
import com.glivion.plasticdiary.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: ProfileFragmentBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var badgesAdapter: BadgesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.profile_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {
        viewModel.getLeaderboard()
        viewModel.getBadges()
        viewModel.loadingDialog.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                if (it) {
                    loadingDialog.startProgressDialog()
                } else {
                    loadingDialog.dismissDialog()
                }
            }
        })

        viewModel.userLoader.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                binding.swipeRefresh.isRefreshing = it
            }
        })

        viewModel.userErrors.observe(viewLifecycleOwner, { isError ->
            isError.let { e ->
                Timber.e("initViewModel: ${e.localizedMessage}")
                showErrorMessage(binding.parentLayout, e)
            }
        })

        viewModel.responses.observe(viewLifecycleOwner, { response ->
            Timber.e("response: $response")
            showSnackBarMessage(binding.parentLayout, response)
        })

        viewModel.badges.observe(viewLifecycleOwner, {
            badgesAdapter.submitList(it)
        })
        viewModel.leaderboard.observe(viewLifecycleOwner, {
            val name = it.leaderboard?.thisWeek?.get(0)?.name
            if (name == viewModel.getUser()?.name) {
             binding.leaderBadge.visibility = View.VISIBLE
            }
        })
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(requireContext())
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        badgesAdapter = object: BadgesAdapter(){
            override fun viewRewardDetails(badges: Badges) {
                val title = bundleOf(REWARD_TITLE to badges)
                findNavController().navigate(R.id.action_profileFragment_to_rewardDetailsFragment, title, options)
            }

        }

        binding.apply {
            user = authViewModel.getCurrentUser()
            bookMarks.setOnClickListener {
                it.findNavController().navigate(R.id.action_profileFragment_to_bookmarksFragment, null, options)
            }
            leaderboard.setOnClickListener {
                it.findNavController().navigate(R.id.action_profileFragment_to_leaderboardFragment, null, options)
            }
            settings.setOnClickListener {
                it.findNavController().navigate(R.id.action_profileFragment_to_settingsFragment, null, options)
            }
            feedback.setOnClickListener {
                openFeedbackDialog()
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.getBadges()
            }
           badgesRecyclerView.apply {
               layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
               adapter = badgesAdapter
           }
        }
    }

    private fun openFeedbackDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.myFullscreenAlertDialogStyle)
        val binding: ProfileFeedbackLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.profile_feedback_layout, null, false)

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        binding.apply {
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            done.setOnClickListener {
                val feedback = feedbackInputField.text.toString()
                if (feedback.isEmpty()) {
                    feedbackInputField.error = "Please enter feedback"
                    return@setOnClickListener
                }
                dialog.dismiss()
                viewModel.submitFeedback(feedback)
            }
        }

        dialog.show()
    }

}