package com.glivion.plasticdiary.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.glivion.plasticdiary.contracts.StatusCallbacks
import com.glivion.plasticdiary.data.repository.QuizRepository
import com.glivion.plasticdiary.model.questions.BaseQuizQuestionsObject
import com.glivion.plasticdiary.model.quiz.BaseQuizObject
import com.glivion.plasticdiary.util.QUIZ_ID
import com.glivion.plasticdiary.util.QUIZ_SCORE
import com.glivion.plasticdiary.util.TERMINATE_QUIZ_TAG_OUTPUT
import com.glivion.plasticdiary.util.TERMINATE_QUIZ_WORK_NAME
import com.glivion.plasticdiary.workers.TerminateQuizWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    val repository: QuizRepository,
    private val application: Application
) : ViewModel() {
    private val _userErrors by lazy { MutableLiveData<Throwable>() }
    val userErrors: LiveData<Throwable>
        get() = _userErrors
    private val _userLoader by lazy { MutableLiveData<Boolean>() }
    val userLoader: LiveData<Boolean>
        get() = _userLoader
    private val _loadingDialog by lazy { MutableLiveData<Boolean>() }
    val loadingDialog: LiveData<Boolean>
        get() = _loadingDialog
    private val _responses by lazy { MutableLiveData<String>() }
    val responses: LiveData<String>
        get() = _responses
    private val _data by lazy { MutableLiveData<BaseQuizObject>() }
    val data: LiveData<BaseQuizObject>
        get() = _data

    private val _questions by lazy { MutableLiveData<BaseQuizQuestionsObject>() }
    val questions: LiveData<BaseQuizQuestionsObject>
        get() = _questions

    private val compositeDisposable by lazy { CompositeDisposable() }

    var terminateQuiz = false
    var categoryID = 0
    var score = 0
    val workManager = WorkManager.getInstance(application)

    init {

    }

    fun getQuizPageItems() {
        _userLoader.postValue(true)
        compositeDisposable.add(
            repository.getQuizPageItems()
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userLoader.postValue(false)
                    _data.postValue(it.body())
                }, { e ->
                    _userLoader.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    fun getQuizQuestions(id: String) {
        _loadingDialog.postValue(true)
        compositeDisposable.add(
            repository.getQuizQuestions(id)
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _loadingDialog.postValue(false)
                    _questions.postValue(it.body())
                }, { e ->
                    _loadingDialog.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    fun submitScore(score: Int, quiz_category_id: Int, listener: StatusCallbacks<String?>) {
        _loadingDialog.postValue(true)
        compositeDisposable.add(
            repository.submitScore(score, quiz_category_id)
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _loadingDialog.postValue(false)
                    listener.onComplete(it.body()?.message)
                }, { e ->
                    _loadingDialog.postValue(false)
                    listener.onFailure(e.localizedMessage)
                    _userErrors.postValue(e)
                })
        )
    }

    fun startWorkerForTerminateQuiz(categoryID: Int, score: Int) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val data = Data.Builder()
            .putInt(QUIZ_ID, categoryID)
            .putInt(QUIZ_SCORE, score)
            .build()
        val terminateQuizRequest = OneTimeWorkRequestBuilder<TerminateQuizWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .addTag(TERMINATE_QUIZ_TAG_OUTPUT)
            .build()

        val continuation = workManager.beginUniqueWork(
            TERMINATE_QUIZ_WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            terminateQuizRequest
        )

        continuation.enqueue()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}