package com.engineerskasa.plasticdiary.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.engineerskasa.plasticdiary.R
import com.engineerskasa.plasticdiary.databinding.ProfileLeaderboardFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderboardFragment : Fragment() {
    private lateinit var binding: ProfileLeaderboardFragmentBinding
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
        return binding.root
    }
}