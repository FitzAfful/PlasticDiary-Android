package com.glivion.plasticdiary.model


data class BaseAuthResponse(
    var error: String? = null,
    var message: String? = null,
    var user: User? = null
)