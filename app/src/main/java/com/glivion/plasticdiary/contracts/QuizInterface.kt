package com.glivion.plasticdiary.contracts

import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.questions.BaseQuizQuestionsObject
import com.glivion.plasticdiary.model.quiz.BaseQuizObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

interface QuizInterface {
    fun getQuizPageItems(): Single<Response<BaseQuizObject>>
    fun getQuizQuestions(id: String): Single<Response<BaseQuizQuestionsObject>>
    fun submitScore(score: Int, quiz_category_id: Int): Single<Response<BaseAuthResponse>>
}