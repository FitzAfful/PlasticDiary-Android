package com.glivion.plasticdiary.view.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ActivityQuizDetailsBinding
import com.glivion.plasticdiary.model.questions.Question
import com.glivion.plasticdiary.model.quiz.Completed
import com.glivion.plasticdiary.util.QUIZ_ID
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.util.showSnackBarMessage
import com.glivion.plasticdiary.view.adapter.quiz.QuestionsAdapter
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class QuizDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizDetailsBinding
    private var completed: Completed? = null
    private val viewModel: QuizViewModel by viewModels()
    private val loadingDialog = LoadingDialog(this)
    private val questions = ArrayList<Question>()
    private lateinit var questionsAdapter: QuestionsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_details)
        binding.lifecycleOwner = this
        getIntentParams()
        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        //viewModel.getQuizQuestions(completed?.id.toString())
        viewModel.getQuizQuestions("5")
        viewModel.loadingDialog.observe(this, { isLoading ->
            isLoading.let {
                if (it) {
                    loadingDialog.startProgressDialog()
                } else {
                    loadingDialog.dismissDialog()
                }
            }
        })
        viewModel.userErrors.observe(this, { isError ->
            isError.let { e ->
                Timber.e("initViewModel: ${e.localizedMessage}")
                showErrorMessage(binding.parentLayout, e)
            }
        })

        viewModel.responses.observe(this, { response ->
            Timber.e("response: $response")
            showSnackBarMessage(binding.parentLayout, response)
        })

        viewModel.questions.observe(this, {
            questions.clear()
            questions.addAll(it.questions!!)
            questionsAdapter.submitList(it.questions)
        })
    }

    private fun initViews() {
        questionsAdapter = QuestionsAdapter(this)
        binding.apply {
            toolbar.title = completed?.name
            toolbar.setNavigationOnClickListener {
                finish()
            }
            quizRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@QuizDetailsActivity, LinearLayoutManager.VERTICAL, false)
                adapter = questionsAdapter
            }
        }
    }

    private fun getIntentParams() {
        intent?.extras?.let {
            completed = it.getParcelable(QUIZ_ID)
        }
    }
}