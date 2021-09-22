package com.glivion.plasticdiary.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ProfileRewardDetailsFragmentBinding
import com.glivion.plasticdiary.model.profile.Badges
import com.glivion.plasticdiary.util.REWARD_TITLE
import com.glivion.plasticdiary.view.adapter.profile.MilestoneAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardDetailsFragment : Fragment() {
    private  lateinit var binding: ProfileRewardDetailsFragmentBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var milestoneAdapter: MilestoneAdapter
    var badge: Badges? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            badge = bundle.getParcelable(REWARD_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_reward_details_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        return binding.root
    }

    private fun initViews() {

        navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)

        milestoneAdapter = MilestoneAdapter()
        binding.apply {
            toolbar.setNavigationOnClickListener {
                navController.navigateUp(appBarConfiguration)
            }
            toolbar.title = badge?.name
            badges = badge
            milestoneRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = milestoneAdapter
            }
        }

        milestoneAdapter.submitList(badge?.milestones)

    }

}