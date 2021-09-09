package com.glivion.plasticdiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glivion.plasticdiary.data.repository.SettingRepository
import com.glivion.plasticdiary.model.profile.BaseLeaderboardObject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: SettingRepository
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

    private val _leaderboard by lazy { MutableLiveData<BaseLeaderboardObject>() }
    val leaderboard: LiveData<BaseLeaderboardObject>
        get() = _leaderboard

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {

    }

    fun submitFeedback(feedback: String) {
        _loadingDialog.postValue(true)
        compositeDisposable.add(
            repository.submitFeedback(feedback)
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _responses.postValue(it.body()?.message)
                    _loadingDialog.postValue(false)
                },{e ->
                    _loadingDialog.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    fun getLeaderboard() {
        _userLoader.postValue(true)
        compositeDisposable.add(
            repository.getLeaderboard()
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userLoader.postValue(false)
                    _leaderboard.postValue(it.body())
                },{e ->
                    _userLoader.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}