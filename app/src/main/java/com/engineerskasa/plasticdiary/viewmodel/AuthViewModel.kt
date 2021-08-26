package com.engineerskasa.plasticdiary.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerskasa.plasticdiary.data.repository.AuthRepository
import com.engineerskasa.plasticdiary.model.User
import com.engineerskasa.plasticdiary.util.currentVersion
import com.engineerskasa.plasticdiary.view.ui.MainActivity
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.Scopes
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    val application: Application
    ): ViewModel() {
    private val _userErrors by lazy { MutableLiveData<String>() }
    val userErrors: LiveData<String>
        get() = _userErrors
    private val _responses by lazy { MutableLiveData<String>() }
    val responses: LiveData<String>
        get() = _responses
    private val _userLoader by lazy { MutableLiveData<Boolean>() }
    val userLoader: LiveData<Boolean>
        get() = _userLoader

    private val compositeDisposable by lazy { CompositeDisposable() }

    val user = User()

    fun signInWithSocialAuthCredentials(
        credential: AuthCredential,
        account: GoogleSignInAccount,
    ): Task<AuthResult> {
        _userLoader.postValue(true)
        return repository.signInWithSocialAuthCredentials(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val authUser = task.result?.user
                        val scope = "oauth2: ${Scopes.EMAIL} ${Scopes.PROFILE}"
                        val accessToken = arrayOf("")
                        try {
                            accessToken[0] = GoogleAuthUtil.getToken(
                               application.applicationContext,
                                account.account,
                                scope,
                                Bundle()
                            )
                            val user = User()
                            user.name = authUser?.displayName
                            user.imgUrl = authUser?.photoUrl.toString()
                            user.platform = currentVersion()
                            user.token = accessToken[0]
                            val userJson = Gson().toJson(user)
                            repository.saveUserPreference(userJson)
                            _userLoader.postValue(false)
                            val intent = Intent(application.applicationContext, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            application.startActivity(intent)
                            //registerUserWithAPI(user)

                        } catch (e: GoogleAuthException) {
                            _userLoader.postValue(false)
                            _userErrors.postValue(e.message)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                _userLoader.postValue(false)
                _userErrors.postValue(e.message)
            }
    }

    private fun registerUserWithAPI(user: User) {
        Timber.e("user $user")
        compositeDisposable.add(
            repository.registerUserWithAPI(user.name!!, user.imgUrl!!, user.token!!, user.platform!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful) {
                        val userJson = Gson().toJson(it.body()?.user)
                        repository.saveUserPreference(userJson)
                        _userLoader.postValue(false)
                        _responses.postValue(it.body()?.message)
                        val intent = Intent(application.applicationContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        application.startActivity(intent)
                    }else {
                        _userLoader.postValue(false)
                       Timber.e("Error: ${it.errorBody()}")
                        if (it.code() == 500){
                            _userErrors.postValue("An unexpected error occurred please try again later")
                        }
                        if (it.code() == 400) {
                            _userErrors.postValue("Some fields were missing. please check and try again")
                        }
                    }
                },{
                    _userLoader.postValue(false)
                    _userErrors.postValue(it.message)
                })
        )
    }

    fun getCurrentUser(): User? = repository.getSignedInUser()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}