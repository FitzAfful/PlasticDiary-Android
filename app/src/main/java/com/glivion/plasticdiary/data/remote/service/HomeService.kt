package com.glivion.plasticdiary.data.remote.service

import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.home.BaseHomeResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.*

interface HomeService {

    @FormUrlEncoded
    @POST("record-usage")
    fun submitUsage(
        @Field("amount") amount: String,
        @Field("date") date: String,
        @Field("token") token: String
    ): Single<Response<BaseAuthResponse>>

    @FormUrlEncoded
    @POST("streak")
    fun streak(
        @Field("minutes") minutes: String,
        @Field("date") date: String,
        @Field("token") token: String
    ): Single<Response<BaseAuthResponse>>

    @FormUrlEncoded
    @POST("open-content")
    fun singleContentAPI(
        @Field("content_id") content_id: Int,
        @Field("content_type") content_type: String,
        @Field("token") token: String
    ): Single<Response<BaseAuthResponse>>

    @FormUrlEncoded
    @POST("bookmark")
    fun bookmark(
        @Field("content_id") content_id: Int,
        @Field("content_type") content_type: String,
        @Field("token") token: String
    ): Single<Response<BaseAuthResponse>>



    @GET("home")
    fun getHomePageItems(@Query("token") token: String): Single<Response<BaseHomeResponse>>
}