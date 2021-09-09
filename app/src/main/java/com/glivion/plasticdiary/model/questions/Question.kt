package com.glivion.plasticdiary.model.questions


import com.google.gson.annotations.SerializedName

data class Question(
    var comment: String? = null,
    var correct: String? = null,
    @SerializedName("option_1")
    var option1: String? = null,
    @SerializedName("option_2")
    var option2: String? = null,
    @SerializedName("option_3")
    var option3: String? = null,
    @SerializedName("option_4")
    var option4: String? = null,
    var question: String? = null
)