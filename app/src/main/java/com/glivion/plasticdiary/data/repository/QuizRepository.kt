package com.glivion.plasticdiary.data.repository

import com.glivion.plasticdiary.data.remote.service.QuizService
import com.glivion.plasticdiary.model.quiz.BaseQuizObject
import com.glivion.plasticdiary.preference.AppPreference
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class QuizRepository @Inject constructor(
    val service: QuizService,
    val appPreference: AppPreference
){
    fun getQuizPageItems(): Single<Response<BaseQuizObject>> = service.getQuizPageItems(appPreference.getUser()?.token.toString())
}