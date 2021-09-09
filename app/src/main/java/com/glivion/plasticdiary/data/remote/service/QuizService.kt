package com.glivion.plasticdiary.data.remote.service

import com.glivion.plasticdiary.model.quiz.BaseQuizObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizService {
    @GET("get-quiz")
    fun getQuizPageItems(@Query("token") token: String): Single<Response<BaseQuizObject>>
}