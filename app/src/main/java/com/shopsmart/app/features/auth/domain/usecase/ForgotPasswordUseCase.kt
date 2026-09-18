package com.shopsmart.app.features.auth.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.repository.AuthRepository

class ForgotPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): AppResult<Unit> =
        repository.forgotPassword(email)
}