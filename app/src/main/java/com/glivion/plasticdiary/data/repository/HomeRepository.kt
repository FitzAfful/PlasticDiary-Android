package com.glivion.plasticdiary.data.repository

import com.glivion.plasticdiary.data.remote.service.HomeService
import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.Streak
import com.glivion.plasticdiary.model.User
import com.glivion.plasticdiary.model.home.BaseHomeResponse
import com.glivion.plasticdiary.model.home.BaseStreakObject
import com.glivion.plasticdiary.preference.AppPreference
import com.glivion.plasticdiary.util.getCurrentDateTimeParams
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val service: HomeService,
    private val appPreference: AppPreference
) {
    fun submitUsage(amount: String): Single<Response<BaseAuthResponse>> = service.submitUsage(
        amount,
        getCurrentDateTimeParams().first,
        appPreference.getUser()?.token!!,
        appPreference.getUser()?.uid!!
    )

    fun updateUserToken(token: String): Single<Response<BaseAuthResponse>> = service.updateUserToken(
        token,
        appPreference.getUser()?.token!!,
        appPreference.getUser()?.uid!!
    )

    fun submitStreak(minutes: String): Single<Response<BaseAuthResponse>> = service.streak(
        minutes,
        getCurrentDateTimeParams().first,
        appPreference.getUser()?.token!!,
        appPreference.getUser()?.uid!!
    )

    fun getUser(): User? = appPreference.getUser()

    fun saveUserPreference(user: String) = appPreference.saveUser(user)


    fun singleContentAPI(contentID: Int, contentType: String): Single<Response<BaseAuthResponse>> =
        service.singleContentAPI(
            contentID,
            contentType,
            appPreference.getUser()?.token!!,
            appPreference.getUser()?.uid!!
        )

    fun bookmark(contentID: Int, contentType: String): Single<Response<BaseAuthResponse>> =
        service.bookmark(
            contentID,
            contentType,
            appPreference.getUser()?.token!!,
            appPreference.getUser()?.uid!!
        )

    fun getHomePageItems(): Single<Response<BaseHomeResponse>> = service.getHomePageItems(
        appPreference.getUser()?.token.toString(),
        appPreference.getUser()?.uid!!
    )

    fun getCurrentStreak(): Single<Response<BaseStreakObject>> = service.getCurrentStreak(
        appPreference.getUser()?.token.toString(),
        appPreference.getUser()?.uid!!
    )

    fun saveCurrentStreak(streak: String) = appPreference.saveStreak(streak)

    fun getUsersCurrentStreak(): Streak? = appPreference.getStreak()
}