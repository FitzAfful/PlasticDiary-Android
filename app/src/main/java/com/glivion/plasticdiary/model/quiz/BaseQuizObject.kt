package com.glivion.plasticdiary.model.quiz


import com.google.gson.annotations.SerializedName

data class BaseQuizObject(
    var average: Double? = null,
    var categories: List<Category>? = null,
    var completed: List<Completed>? = null,
    @SerializedName("quizzes_taken")
    var quizzesTaken: QuizzesTaken? = null
)