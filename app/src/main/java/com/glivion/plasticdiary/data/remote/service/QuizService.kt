package com.glivion.plasticdiary.data.remote.service

import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.questions.BaseQuizQuestionsObject
import com.glivion.plasticdiary.model.quiz.BaseQuizObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.*

interface QuizService {
    @GET("get-quiz")
    fun getQuizPageItems(@Query("token") token: String, @Query("uid") uid: String): Single<Response<BaseQuizObject>>

    @GET("get-question")
    fun getQuizQuestions(
        @Query("token") token: String,
        @Query("uid") uid: String,
        @Query("question_category_id") question_category_id: String
    ): Single<Response<BaseQuizQuestionsObject>>

    @FormUrlEncoded
    @POST("record-quiz")
    fun submitScore(
        @Field("score") score: Int,
        @Field("quiz_category_id") quiz_category_id: Int,
        @Field("token") token: String,
        @Field("uid") uid: String
    ): Single<Response<BaseAuthResponse>>
}