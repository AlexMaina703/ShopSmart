package com.shopsmart.app.features.auth.presentation.state

import com.shopsmart.app.features.auth.domain.model.User

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User? = null) : AuthState()
    data class Error(val message: String) : AuthState()
}
