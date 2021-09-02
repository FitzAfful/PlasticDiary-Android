package com.glivion.plasticdiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glivion.plasticdiary.data.repository.HomeRepository
import com.glivion.plasticdiary.model.home.BaseHomeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
private val repository: HomeRepository
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
    private val _data by lazy { MutableLiveData<BaseHomeResponse>() }
    val data: LiveData<BaseHomeResponse>
        get() = _data

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        submitStreak("1")
        getHomePageItems()
    }

    fun getHomePageItems(){
        _userLoader.postValue(true)
        compositeDisposable.add(
            repository.getHomePageItems()
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

    fun submitUsage(amount: String) {
        _loadingDialog.postValue(true)
        compositeDisposable.add(
            repository.submitUsage(amount)
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
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

    private fun submitStreak(minutes: String) {
        compositeDisposable.add(
            repository.submitStreak(minutes)
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _responses.postValue(it.body()?.message)
                },{e ->
                    _userErrors.postValue(e)
                })
        )
    }

    override fun onCleared() {
        Timber.e("on clear called")
        super.onCleared()
        compositeDisposable.dispose()
    }
}