package com.engineerskasa.plasticdiary.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.engineerskasa.plasticdiary.R
import com.engineerskasa.plasticdiary.databinding.HomeFragmentBinding
import com.engineerskasa.plasticdiary.databinding.MyUsageLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.home_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.apply {
            recordUsage.setOnClickListener {
                openUsageDialog()
            }
        }
    }

    private fun openUsageDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.myFullscreenAlertDialogStyle)
        val binding: MyUsageLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.my_usage_layout, null, false)

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        binding.apply {
            cancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}