package com.glivion.plasticdiary.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.QuizFragmentBinding
import com.glivion.plasticdiary.model.quiz.Category
import com.glivion.plasticdiary.view.adapter.quiz.QuizPageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment() {
    private lateinit var binding: QuizFragmentBinding
    private lateinit var categoryAdapter: QuizPageAdapter
    private lateinit var completedAdapter: QuizPageAdapter
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
           /* performance.
                apply {
                    placeholder = "80%"
                    description = "Average\nPerformance"
                    type = "performance"
                }

            quizzes.apply {
                placeholder = "5/15"
                description = "Quizzes\nTaken"
                type = "quiz"
            }*/
            categoryAdapter = object : QuizPageAdapter("category"){
                override fun startQuiz(category: Category) {

                }

                override fun viewResults(category: Category) {

                }

            }
            completedAdapter = object : QuizPageAdapter("completed"){
                override fun startQuiz(category: Category) {

                }

                override fun viewResults(category: Category) {

                }

            }
            categoriesRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = categoryAdapter
            }
            completedRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = completedAdapter
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}