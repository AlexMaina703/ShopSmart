package com.shopsmart.app.features.auth.domain.repository

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.model.AuthProvider
import com.shopsmart.app.features.auth.domain.model.AuthResult
import com.shopsmart.app.features.auth.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<AuthResult>
    suspend fun register(fullName: String, email: String, password: String, phone: String): AppResult<AuthResult>
    suspend fun validateToken(): AppResult<User>
    suspend fun loadToken(): AppResult<Unit>
    suspend fun logout(): AppResult<Unit>
    suspend fun isLoggedIn(): AppResult<Boolean>

    suspend fun socialLogin(
        provider: AuthProvider,
        accessToken: String
    ): AppResult<AuthResult>


    suspend fun hasCompletedOnboarding(): AppResult<Boolean>
    suspend fun setOnboardingComplete(): AppResult<Unit>

    suspend fun forgotPassword(email: String): AppResult<Unit>
    suspend fun resetPassword(token: String, newPassword: String): AppResult<Unit>
}