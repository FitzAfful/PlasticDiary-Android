package com.engineerskasa.plasticdiary.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.engineerskasa.plasticdiary.R
import com.engineerskasa.plasticdiary.databinding.ProfileFeedbackLayoutBinding
import com.engineerskasa.plasticdiary.databinding.ProfileFragmentBinding
import com.engineerskasa.plasticdiary.util.REWARD_TITLE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.profile_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        return binding.root
    }

    private fun initViews() {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        binding.apply {
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
            rewardViewer.setOnClickListener {
                val title = bundleOf(REWARD_TITLE to "Viewer")
                it.findNavController().navigate(R.id.action_profileFragment_to_rewardDetailsFragment, title, options)
            }
            rewardBookmark.setOnClickListener {
                val title = bundleOf(REWARD_TITLE to "Bookmark")
                it.findNavController().navigate(R.id.action_profileFragment_to_rewardDetailsFragment, title, options)
            }
            rewardBrilliant.setOnClickListener {
                val title = bundleOf(REWARD_TITLE to "Brilliant")
                it.findNavController().navigate(R.id.action_profileFragment_to_rewardDetailsFragment, title, options)
            }
            rewardWatcher.setOnClickListener {
                val title = bundleOf(REWARD_TITLE to "Watcher")
                it.findNavController().navigate(R.id.action_profileFragment_to_rewardDetailsFragment, title, options)
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
                dialog.dismiss()
            }
        }

        dialog.show()
    }

}