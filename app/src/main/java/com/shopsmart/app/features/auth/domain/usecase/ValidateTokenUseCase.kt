package com.shopsmart.app.features.auth.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.model.User
import com.shopsmart.app.features.auth.domain.repository.AuthRepository

class ValidateTokenUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): AppResult<User> = repository.validateToken()
}