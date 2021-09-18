package com.glivion.plasticdiary.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.CurrentStreakBottomSheetLayoutBinding
import com.glivion.plasticdiary.util.getDaysOfTheWeek
import com.glivion.plasticdiary.util.getToday
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StreakBottomSheetDialog(
    private val context: Context,
    private val streak: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: CurrentStreakBottomSheetLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.current_streak_bottom_sheet_layout,
            container,
            false
        )
        initViews()
        return binding.root
    }

    private fun initViews() {
        val days = getDaysOfTheWeek()
        val today = getToday()
        binding.apply {
            monTxt.text = days[0]
            tueTxt.text = days[1]
            wedTxt.text = days[2]
            thuTxt.text = days[3]
            friTxt.text = days[4]
            satTxt.text = days[5]
            sunTxt.text = days[6]
        }
        when (today) {
            binding.sunTxt.text.toString() -> {
               binding.sunTxt.background = ContextCompat.getDrawable(context, R.drawable.bg_green_circle)
            }
            binding.monTxt.text.toString() -> {
                binding.monTxt.background = ContextCompat.getDrawable(context, R.drawable.bg_green_circle)
            }
            binding.tueTxt.text.toString() -> {
                binding.tueTxt.background = ContextCompat.getDrawable(context, R.drawable.bg_green_circle)
            }
            binding.wedTxt.text.toString() -> {
                binding.wedTxt.background = ContextCompat.getDrawable(context, R.drawable.bg_green_circle)
            }
            binding.thuTxt.text.toString() -> {
                binding.thuTxt.background = ContextCompat.getDrawable(context, R.drawable.bg_green_circle)
            }
            binding.friTxt.text.toString() -> {
                binding.friTxt.background = ContextCompat.getDrawable(context, R.drawable.bg_green_circle)
            }
            binding.satTxt.text.toString() -> {
                binding.satTxt.background = ContextCompat.getDrawable(context, R.drawable.bg_green_circle)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let {
            it.setCanceledOnTouchOutside(true)
            it.setCancelable(true)
            isCancelable = true
        }
        binding.apply {
            totalStreaks.text = "$streak days"
            streakInfo.text = "$streak days in this app this year"
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}