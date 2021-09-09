package com.glivion.plasticdiary.model.questions

data class Question(
    var comment: String? = null,
    var question: String? = null,
    var responses: List<Response>? = null
)
