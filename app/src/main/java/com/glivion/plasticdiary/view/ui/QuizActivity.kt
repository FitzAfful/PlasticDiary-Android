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
import com.glivion.plasticdiary.view.dialog.AnswerBottomSheetDialog
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class QuizActivity : AppCompatActivity(), AnswerBottomSheetDialog.ItemClickListener {
    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()
    private var category: Category? = null
    private val loadingDialog = LoadingDialog(this)
    private val questions = ArrayList<Question>()
    var score = 0
    var index = -1
    var thisQuestion = 0
    var totalQuestions = 0
    var correctAnswer = 0
    private lateinit var dialog: AnswerBottomSheetDialog
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
            optionA.setOnClickListener {
                showAnswerDialog(index, optionA.text.toString())
            }
            optionB.setOnClickListener {
                showAnswerDialog(index, optionB.text.toString())
            }
            optionC.setOnClickListener {
                showAnswerDialog(index, optionC.text.toString())
            }
            optionD.setOnClickListener {
                showAnswerDialog(index, optionD.text.toString())
            }
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
            totalQuestions = questions.size
            showQuestions(++index)
            Timber.e("questions: ${questions.size}")
        })
    }

    private fun showQuestions(index: Int) {
        if (index < totalQuestions) {
            thisQuestion++
            binding.apply {
                progressCount.text = "Question $thisQuestion of $totalQuestions"
                question.text = questions[index].question
                optionA.text = questions[index].option1
                optionB.text = questions[index].option2
                optionC.text = questions[index].option3
                optionD.text = questions[index].option4
            }
        } /*else {
            Toast.makeText(this, "Quiz Completed", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun showAnswerDialog(index: Int, option: String) {
        var status = false
        if (option == questions[index].correct) {
            status = true
            score += 1
        }
        dialog = AnswerBottomSheetDialog(status, questions[index].comment.toString())
        dialog.showNow(supportFragmentManager, "AnswerBottomSheetDialog")
    }

    override fun continueQuiz() {
        dialog.dismiss()
        if (index < totalQuestions) {
            showQuestions(++index)
        }
    }
}