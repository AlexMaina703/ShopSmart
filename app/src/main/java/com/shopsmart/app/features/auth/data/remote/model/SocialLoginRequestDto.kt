package com.shopsmart.app.features.auth.data.remote.model

data class SocialLoginRequestDto(
    val provider: String,
    val accessToken: String
)