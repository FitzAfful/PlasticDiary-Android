package com.glivion.plasticdiary.data.remote.service

import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.profile.BaseBadgesObject
import com.glivion.plasticdiary.model.profile.BaseLeaderboardObject
import com.glivion.plasticdiary.model.profile.BaseUserObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.*

interface SettingService {
    @FormUrlEncoded
    @POST("send-feedback")
    fun submitFeedback(
        @Field("message") message: String,
        @Field("feedback_option") feedback_option: String,
        @Field("token") token: String
    ): Single<Response<BaseAuthResponse>>

    @GET("leaderboard")
    fun getLeaderboard(@Query("token") token: String): Single<Response<BaseLeaderboardObject>>

    @GET("profile")
    fun getUserProfileAndBookmarks(@Query("token") token: String): Single<Response<BaseUserObject>>

    @GET("badges")
    fun getBadges(@Query("token") token: String): Single<Response<BaseBadgesObject>>
}