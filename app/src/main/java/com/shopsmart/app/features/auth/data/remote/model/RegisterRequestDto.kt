package com.shopsmart.app.features.auth.data.remote.model

data class RegisterRequestDto(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String
)