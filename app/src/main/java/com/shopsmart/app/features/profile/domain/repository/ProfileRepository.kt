package com.shopsmart.app.features.profile.domain.repository


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): AppResult<Profile>
    suspend fun updateProfile(
        fullName: String,
        phone: String?,
        avatarUrl: String?,
    ): AppResult<Profile>
}