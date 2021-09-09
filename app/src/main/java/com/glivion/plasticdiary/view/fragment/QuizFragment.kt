package com.glivion.plasticdiary.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.QuizFragmentBinding
import com.glivion.plasticdiary.model.quiz.Category
import com.glivion.plasticdiary.model.quiz.Completed
import com.glivion.plasticdiary.model.quiz.QuizInterface
import com.glivion.plasticdiary.util.QUIZ_ID
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.util.showSnackBarMessage
import com.glivion.plasticdiary.view.adapter.quiz.QuizPageAdapter
import com.glivion.plasticdiary.view.ui.QuizActivity
import com.glivion.plasticdiary.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class QuizFragment : Fragment() {
    private lateinit var binding: QuizFragmentBinding
    private lateinit var categoryAdapter: QuizPageAdapter
    private lateinit var completedAdapter: QuizPageAdapter
    private val viewModel: QuizViewModel by viewModels()
    private val categoryList = ArrayList<Category>()
    private val completedList = ArrayList<Completed>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.quiz_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {
        viewModel.userLoader.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                binding.swipeRefresh.isRefreshing = it
            }
        })
        viewModel.userErrors.observe(viewLifecycleOwner, { isError ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                isError.let { e ->
                    Timber.e("initViewModel: ${e.localizedMessage}")
                    binding.swipeRefresh.isRefreshing = false
                    showErrorMessage(binding.parentLayout, e)
                }
            }
        })


        viewModel.responses.observe(viewLifecycleOwner, { response ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.swipeRefresh.isRefreshing = false
                Timber.e("response: $response")
                showSnackBarMessage(binding.parentLayout, response)
            }
        })
        viewModel.data.observe(viewLifecycleOwner, {
            binding.apply {
                performance.
                apply {
                    placeholder = "${it.average.toString()}%"
                    description = "Average\nPerformance"
                }

                quizzes.apply {
                    placeholder = "${it.quizzesTaken?.taken}/${it.quizzesTaken?.totalQuizzes}"
                    description = "Quizzes\nTaken"

                }
            }
            categoryAdapter.submitList(it.categories)
            completedAdapter.submitList(it.completed)
        })
    }

    private fun initViews() {
        binding.apply {
            performance.apply {
                type = "performance"
            }
            quizzes.apply {
                type = "quiz"
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.getQuizPageItems()
            }
            categoryAdapter = object : QuizPageAdapter("category") {
                override fun startQuiz(category: QuizInterface) {
                    Intent(requireContext(), QuizActivity::class.java).apply {
                        putExtra(QUIZ_ID, category as Category)
                        startActivity(this)
                    }
                }

                override fun viewResults(category: QuizInterface) {

                }

            }
            completedAdapter = object : QuizPageAdapter("completed") {
                override fun startQuiz(category: QuizInterface) {

                }

                override fun viewResults(category: QuizInterface) {

                }

            }
            categoriesRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = categoryAdapter
            }
            completedRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = completedAdapter
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}