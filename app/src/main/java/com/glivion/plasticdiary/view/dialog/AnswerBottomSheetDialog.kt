package com.glivion.plasticdiary.view.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.QuizRightOrWrongBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AnswerBottomSheetDialog(
    private val status: Boolean,
    private val comments: String
) : BottomSheetDialogFragment() {

    private lateinit var binding: QuizRightOrWrongBottomSheetLayoutBinding
    private var listener: ItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.quiz_right_or_wrong_bottom_sheet_layout,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let {
            it.setCanceledOnTouchOutside(false)
            it.setCancelable(false)
            isCancelable = false
        }
        binding.apply {
            if (!status) {
                continueBtn.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.logout_btn_color
                    )
                )
            }
            comment.text = comments
            continueBtn.setOnClickListener {
                listener?.continueQuiz()
            }
        }
    }

    interface ItemClickListener {
        fun continueQuiz()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ItemClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}