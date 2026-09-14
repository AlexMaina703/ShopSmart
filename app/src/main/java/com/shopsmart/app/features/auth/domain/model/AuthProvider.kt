package com.shopsmart.app.features.auth.domain.model

enum class AuthProvider(val value: String) {
    EMAIL("email"),
    GOOGLE("google"),
    FACEBOOK("facebook");
}