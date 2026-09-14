package com.shopsmart.app.features.auth.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.model.AuthProvider
import com.shopsmart.app.features.auth.domain.model.AuthResult
import com.shopsmart.app.features.auth.domain.repository.AuthRepository

class SocialLoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        provider: AuthProvider,
        accessToken: String
    ): AppResult<AuthResult> = repository.socialLogin(provider, accessToken)
}