package com.glivion.plasticdiary.data.repository

import com.glivion.plasticdiary.data.remote.service.AuthService
import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.User
import com.glivion.plasticdiary.preference.AppPreference
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val appPreference: AppPreference,
    private val authService: AuthService
) {
    fun signInWithSocialAuthCredentials(credential: AuthCredential): Task<AuthResult> =
        firebaseAuth.signInWithCredential(credential)

    fun registerUserWithAPI(
        name: String,
        image_url: String,
        token: String,
        uid: String,
        message_token: String,
        platform: String
    ): Single<Response<BaseAuthResponse>> =
        authService.registerUser(name, image_url, token, uid, message_token, platform)

    fun saveUserPreference(user: String) = appPreference.saveUser(user)

    fun getSignedInUser(): User? = appPreference.getUser()
}