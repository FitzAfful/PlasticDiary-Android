package com.glivion.plasticdiary.data.remote.service

import com.glivion.plasticdiary.model.BaseAuthResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SettingService {
    @FormUrlEncoded
    @POST("send-feedback")
    fun submitFeedback(
        @Field("message") message: String,
        @Field("feedback_option") feedback_option: String,
        @Field("token") token: String
    ): Single<Response<BaseAuthResponse>>
}