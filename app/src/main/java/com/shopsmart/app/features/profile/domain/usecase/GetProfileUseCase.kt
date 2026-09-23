package com.shopsmart.app.features.profile.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.profile.domain.model.Profile
import com.shopsmart.app.features.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val repo: ProfileRepository) {
    suspend operator fun invoke(): AppResult<Profile> = repo.getProfile()
}