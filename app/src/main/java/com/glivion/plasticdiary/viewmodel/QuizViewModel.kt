package com.glivion.plasticdiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glivion.plasticdiary.data.repository.QuizRepository
import com.glivion.plasticdiary.model.questions.BaseQuizQuestionsObject
import com.glivion.plasticdiary.model.quiz.BaseQuizObject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class QuizViewModel  @Inject constructor(
    val repository: QuizRepository
): ViewModel() {
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

    init {
        getQuizPageItems()
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
                },{e ->
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
                },{e ->
                    _loadingDialog.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}