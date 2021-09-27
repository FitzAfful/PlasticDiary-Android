package com.glivion.plasticdiary.model


data class BaseAuthResponse(
    var error: String? = null,
    var message: String? = null,
    var usage_category: String? = null,
    var max_streak: Int = 0,
    var user: User? = null
)