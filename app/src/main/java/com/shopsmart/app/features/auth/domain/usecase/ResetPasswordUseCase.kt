package com.shopsmart.app.features.auth.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.repository.AuthRepository

class ResetPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(token: String, newPassword: String): AppResult<Unit> =
        repository.resetPassword(token, newPassword)
}