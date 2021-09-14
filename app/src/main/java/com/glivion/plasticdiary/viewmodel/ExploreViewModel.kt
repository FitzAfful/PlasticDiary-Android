package com.glivion.plasticdiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glivion.plasticdiary.data.repository.ExploreRepository
import com.glivion.plasticdiary.model.explore.Map
import com.glivion.plasticdiary.model.explore.Resource
import com.glivion.plasticdiary.model.explore.Tip
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: ExploreRepository
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
    private val _tipsData by lazy { MutableLiveData<List<Tip>>() }
    val tipData: LiveData<List<Tip>>
        get() = _tipsData
    private val _resourcesData by lazy { MutableLiveData<List<Resource>>() }
    val resourcesData: LiveData<List<Resource>>
        get() = _resourcesData
    private val _mapItemsData by lazy { MutableLiveData<List<Map>>() }
    val mapItemsData: LiveData<List<Map>>
        get() = _mapItemsData

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        getExplorePageItems()
    }


    fun getExplorePageItems() {
        _userLoader.postValue(true)
        compositeDisposable.add(
            repository.getExplorePageItems()
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }
                .flatMapCompletable {
                    val response = it.body()
                    _tipsData.postValue(response?.tips)
                    _resourcesData.postValue(response?.resource)
                    _mapItemsData.postValue(response?.mapItem)
                    repository.insertTips(response?.tips!!)
                        .andThen(Completable.defer { repository.insertResources(response.resource!!) })
                        .andThen(Completable.defer { repository.insertMapItems(response.mapItem!!) })
                        .subscribeOn(Schedulers.io())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userLoader.postValue(false)
                   // _responses.postValue("updated")
                }, { e ->
                    _userLoader.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    fun submitTip(tip: String) {
        _loadingDialog.postValue(true)
        compositeDisposable.add(
            repository.submitTip(tip)
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
                }, { e ->
                    _loadingDialog.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    fun getAllMapItems() {
        compositeDisposable.add(
            repository.getAllMapItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _mapItemsData.postValue(it)
                },{ e ->
                    _userErrors.postValue(e)
                })
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}