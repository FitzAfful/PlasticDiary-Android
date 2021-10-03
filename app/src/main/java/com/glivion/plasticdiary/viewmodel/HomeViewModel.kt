package com.glivion.plasticdiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glivion.plasticdiary.data.repository.HomeRepository
import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.Streak
import com.glivion.plasticdiary.model.home.BaseHomeResponse
import com.glivion.plasticdiary.util.getCurrentDateTimeParams
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
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
    private val _streak by lazy { MutableLiveData<Int>() }
    val streak: LiveData<Int>
        get() = _streak
    private val _category by lazy { MutableLiveData<BaseAuthResponse>() }
    val category: LiveData<BaseAuthResponse>
        get() = _category

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        getUserMessagingToken()
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

    fun getCurrentStreak(){
        compositeDisposable.add(
            repository.getCurrentStreak()
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _streak.postValue(it.body()?.get(0)?.maxStreak)
                },{e ->
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
                    _category.postValue(it.body())
                    _loadingDialog.postValue(false)
                },{e ->
                    _loadingDialog.postValue(false)
                    _userErrors.postValue(e)
                })
        )
    }

    fun singleContentAPI(contentID: Int, contentType: String) {
        compositeDisposable.add(
            repository.singleContentAPI(contentID, contentType)
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                   // _responses.postValue(it.body()?.message)
                },{e ->
                    _userErrors.postValue(e)
                })
        )
    }

    fun bookmark(contentID: Int, contentType: String) {
        _loadingDialog.postValue(true)
        compositeDisposable.add(
            repository.bookmark(contentID, contentType)
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
                    _userErrors.postValue(e)
                    _loadingDialog.postValue(false)
                })
        )
    }

    fun submitStreak(minutes: String) {
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
                   // _responses.postValue(it.body()?.message)
                },{e ->
                    Timber.e("submitStreak: ${e.message}")
                    //_userErrors.postValue(e)
                })
        )
    }

     fun saveCurrentStreak() {
        val currentStreak = repository.getUsersCurrentStreak()
        val today = getCurrentDateTimeParams().first
        val yesterday = getCurrentDateTimeParams().fifth
        var points = 0
        if (currentStreak?.lastVisited == null) {
            points += 1
        } else {
            points = if (currentStreak.lastVisited != yesterday) {
                1
            } else {
                currentStreak.points!!.toInt() + 1
            }
        }
         val streak = Streak(points.toString(), today)
         val streakJson = Gson().toJson(streak)
         repository.saveCurrentStreak(streakJson)
         _streak.postValue(points)
    }

    private fun getUserMessagingToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    val token = task.result
                    if (repository.getUser()?.message_token != token) {
                        val user = repository.getUser()
                        user?.message_token = token
                        val userJson = Gson().toJson(user)
                        repository.saveUserPreference(userJson)
                        updateUserToken(token!!)
                    }
                    Timber.e("FCMToken: $token")
                }
            }
            .addOnFailureListener { e: Exception ->
                Timber.e(e.message)
            }
    }

    private fun updateUserToken(token: String) {
        compositeDisposable.add(
            repository.updateUserToken(token)
                .filter {
                    if (it.isSuccessful) {
                        true
                    } else {
                        throw HttpException(it)
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                   Timber.e("message: ${it.body()?.message}")
                },{e ->
                    _userErrors.postValue(e)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}