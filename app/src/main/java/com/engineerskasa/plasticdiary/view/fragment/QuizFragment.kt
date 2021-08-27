package com.engineerskasa.plasticdiary.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.engineerskasa.plasticdiary.R
import com.engineerskasa.plasticdiary.databinding.QuizFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment() {
    private lateinit var binding: QuizFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.quiz_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.apply {
            performance.
                apply {
                    placeholder = "80%"
                    description = "Average\nPerformance"
                    type = "performance"
                }

            quizzes.apply {
                placeholder = "5/15"
                description = "Quizzes\nTaken"
                type = "quiz"
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}