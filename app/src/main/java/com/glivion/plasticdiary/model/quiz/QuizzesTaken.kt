package com.glivion.plasticdiary.model.quiz

import com.google.gson.annotations.SerializedName

data class QuizzesTaken(
    var taken: Int? = null,
    @SerializedName("total_quizzes")
    var totalQuizzes: Int? = null
)
