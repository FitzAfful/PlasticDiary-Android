package com.glivion.plasticdiary.data.remote.service

import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.explore.BaseExploreResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ExploreService {
    @GET("explore")
    fun getExplorePageItems(): Single<Response<BaseExploreResponse>>

    @FormUrlEncoded
    @POST("submit-tip")
    fun submitTip(
        @Field("tip") tip: String,
        @Field("token") token: String
    ): Single<Response<BaseAuthResponse>>
}