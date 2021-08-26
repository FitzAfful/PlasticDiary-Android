package com.engineerskasa.plasticdiary.view.fragment.profile

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
import com.engineerskasa.plasticdiary.R
import com.engineerskasa.plasticdiary.databinding.ProfileSettingsFragmentBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: ProfileSettingsFragmentBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_settings_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        return binding.root
    }

    private fun initViews() {
        navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                navController.navigateUp(appBarConfiguration)
            }
            time.setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(10)
                        .setTitleText("Select Notification time")
                        .build()

                picker.addOnPositiveButtonClickListener {
                    time.text = "${picker.hour}:${picker.minute}"
                }

                picker.show(requireActivity().supportFragmentManager, "tag")
            }
        }

    }
}