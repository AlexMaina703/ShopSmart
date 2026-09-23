package com.shopsmart.app.features.profile.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.profile.domain.model.Profile
import com.shopsmart.app.features.profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(private val repo: ProfileRepository) {
    suspend operator fun invoke(
        fullName: String,
        phone: String?,
        avatarUrl: String?,
    ): AppResult<Profile> = repo.updateProfile(fullName, phone, avatarUrl)
}