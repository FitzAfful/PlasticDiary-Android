package com.glivion.plasticdiary.view.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.contracts.StatusCallbacks
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
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class QuizActivity : AppCompatActivity(), AnswerBottomSheetDialog.ItemClickListener {
    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()
    private var category: Category? = null
    private val loadingDialog = LoadingDialog(this)
    private val questions = ArrayList<Question>()
    var selectedButton: MaterialButton? = null
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
                if (questions.isNotEmpty()) {
                    viewModel.submitScore(score, category!!.id, object : StatusCallbacks<String?> {
                        override fun onComplete(data: String?) {
                            showSnackBarMessage(binding.parentLayout, data)
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(1500)
                                finish()
                            }
                        }

                        override fun onFailure(data: String?) {
                            showSnackBarMessage(binding.parentLayout, data)
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(1500)
                                finish()
                            }
                        }
                    })
                } else {
                    finish()
                }

            }
            optionA.setOnClickListener {
                showAnswerDialog(index, optionA.text.toString(), optionA)
            }
            optionB.setOnClickListener {
                showAnswerDialog(index, optionB.text.toString(), optionB)
            }
            optionC.setOnClickListener {
                showAnswerDialog(index, optionC.text.toString(), optionC)
            }
            optionD.setOnClickListener {
                showAnswerDialog(index, optionD.text.toString(), optionD)
            }
        }

    }

    private fun getIntentParams() {
        intent?.extras?.let {
            category = it.getParcelable(QUIZ_ID)
        }
    }

    private fun initViewModel() {
        viewModel.getQuizQuestions(category?.id.toString())
        viewModel.terminateQuiz = true
        viewModel.categoryID = category!!.id
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
        })
    }

    private fun showQuestions(index: Int) {
        if (questions.isEmpty()) {
            binding.optionsLyt.visibility = View.GONE
            showSnackBarMessage(binding.parentLayout, "No questions available for this category")
            CoroutineScope(Dispatchers.IO).launch {
                delay(1500)
                finish()
            }
            return
        }
        if (index < totalQuestions) {
            thisQuestion++
            val progress = (thisQuestion.toFloat() / totalQuestions.toFloat()) * 100
            binding.apply {
                currentScore.text = "Score: $score"
                viewModel.score = score
                progressBar.progress = progress.toInt()
                progressCount.text = "Question $thisQuestion of $totalQuestions"
                question.text = questions[index].question
                optionA.text = questions[index].option1
                optionB.text = questions[index].option2
                optionC.text = questions[index].option3
                optionD.text = questions[index].option4
            }
        } else {
            binding.apply {
                currentScore.text = "Score: $score"
                optionsLyt.visibility = View.GONE
            }
            viewModel.submitScore(score, category!!.id, object: StatusCallbacks<String?>{
                override fun onComplete(data: String?) {
                    showSnackBarMessage(binding.parentLayout, data)
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1500)
                        finish()
                    }
                }

                override fun onFailure(data: String?) {
                    showSnackBarMessage(binding.parentLayout, data)
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1500)
                        finish()
                    }
                }
            })
        }
    }

    private fun showAnswerDialog(index: Int, option: String, optionBtn: MaterialButton) {
        var status = false
        selectedButton = optionBtn
        if (option == questions[index].correct) {
            status = true
            score += 1
            optionBtn.strokeColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this,
                    R.color.heading_text_green
                )
            )
        } else {
            optionBtn.strokeColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this,
                    R.color.logout_btn_color
                )
            )
        }
        dialog = AnswerBottomSheetDialog(status, questions[index].comment.toString())
        dialog.showNow(supportFragmentManager, "AnswerBottomSheetDialog")
    }

    override fun continueQuiz() {
        selectedButton?.strokeColor = ColorStateList.valueOf(
            ContextCompat.getColor(
                this,
                R.color.card_bg_grey
            )
        )
        dialog.dismiss()
        if (index < totalQuestions) {
            showQuestions(++index)
        }
    }

    override fun onBackPressed() {
        if (questions.isNotEmpty()) {
            viewModel.startWorkerForTerminateQuiz(category!!.id, score)
        }
        super.onBackPressed()
    }

    override fun onPause() {
        Timber.e("onPause")
        if (questions.isNotEmpty()) {
            viewModel.startWorkerForTerminateQuiz(category!!.id, score)
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(1500)
            finish()
        }
        super.onPause()
    }

}