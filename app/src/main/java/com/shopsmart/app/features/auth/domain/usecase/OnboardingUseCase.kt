package com.shopsmart.app.features.auth.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.repository.AuthRepository

class OnboardingUseCase(private val repository: AuthRepository) {
    suspend fun isCompleted(): AppResult<Boolean> = repository.hasCompletedOnboarding()
    suspend fun complete(): AppResult<Unit> = repository.setOnboardingComplete()
}