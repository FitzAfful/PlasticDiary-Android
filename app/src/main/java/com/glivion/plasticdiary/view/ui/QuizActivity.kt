package com.glivion.plasticdiary.view.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ActivityQuizBinding
import com.glivion.plasticdiary.model.questions.Question
import com.glivion.plasticdiary.model.quiz.Category
import com.glivion.plasticdiary.util.QUIZ_ID
import com.glivion.plasticdiary.util.setSystemBarColor
import com.glivion.plasticdiary.util.showErrorMessage
import com.glivion.plasticdiary.util.showSnackBarMessage
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()
    private var category: Category? = null
    private val loadingDialog = LoadingDialog(this)
    private val questions = ArrayList<Question>()
    override fun onCreate(savedInstanceState: Bundle?) {
        setSystemBarColor(this, R.color.bg_white)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        binding.lifecycleOwner = this
        getIntentParams()
        initViews()
        initViewModel()
    }

    private fun initViews() {
        binding.apply {
            quizTitle.text = category?.name
            close.setOnClickListener {
                finish()
            }
            progressCount.text = "Question 1 of 10"
        }
    }

    private fun getIntentParams() {
        intent?.extras?.let {
            category = it.getParcelable(QUIZ_ID)
        }
        Timber.e("categoryID: ${category}")
    }

    private fun initViewModel() {
        viewModel.getQuizQuestions(category?.id.toString())
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
            Timber.e("questions: ${questions.size}")
        })
    }
}