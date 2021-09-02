package com.glivion.plasticdiary.data.repository

import com.glivion.plasticdiary.data.remote.service.SettingService
import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.preference.AppPreference
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val service: SettingService,
    private val appPreference: AppPreference
){
    fun submitFeedback(feedback: String): Single<Response<BaseAuthResponse>> = service.submitFeedback(feedback,
        "suggestion", appPreference.getUser()?.token!!)
}