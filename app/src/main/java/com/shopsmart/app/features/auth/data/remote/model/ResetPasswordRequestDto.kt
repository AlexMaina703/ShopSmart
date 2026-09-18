package com.shopsmart.app.features.auth.data.remote.model

data class ResetPasswordRequestDto(
    val token: String,
    val newPassword: String
)