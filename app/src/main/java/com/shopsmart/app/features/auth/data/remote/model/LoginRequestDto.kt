package com.shopsmart.app.features.auth.data.remote.model

data class LoginRequestDto(
    val email: String,
    val password: String
)