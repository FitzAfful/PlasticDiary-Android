package com.glivion.plasticdiary.data.remote.service

import com.glivion.plasticdiary.model.BaseAuthResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("img_url") img_url: String,
        @Field("token") token: String,
        @Field("platform") platform: String
    ): Single<Response<BaseAuthResponse>>
}