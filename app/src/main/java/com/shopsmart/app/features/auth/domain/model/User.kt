package com.shopsmart.app.features.auth.domain.model

data class  User(
    val id : String,
    val email: String,
    val fullName: String,
    val phone: String?,
    val avatarUrl: String?,
    val provider: String,
    val emailVerifiedAt: Boolean,
    val createdAt: String,
)