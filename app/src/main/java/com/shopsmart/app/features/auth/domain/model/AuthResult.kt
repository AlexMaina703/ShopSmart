package com.shopsmart.app.features.auth.domain.model


data class AuthResult(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: User? = null
)