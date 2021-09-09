package com.glivion.plasticdiary.data.repository

import com.glivion.plasticdiary.data.remote.service.HomeService
import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.home.BaseHomeResponse
import com.glivion.plasticdiary.preference.AppPreference
import com.glivion.plasticdiary.util.getCurrentDateTimeParams
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val service: HomeService,
    private val appPreference: AppPreference
) {
    fun submitUsage(amount: String): Single<Response<BaseAuthResponse>> = service.submitUsage(amount,
        getCurrentDateTimeParams().first, appPreference.getUser()?.token!!)

    fun submitStreak(minutes: String): Single<Response<BaseAuthResponse>> = service.streak(minutes,
        getCurrentDateTimeParams().first, appPreference.getUser()?.token!!)

    fun getHomePageItems(): Single<Response<BaseHomeResponse>> = service.getHomePageItems(appPreference.getUser()?.token.toString())
}