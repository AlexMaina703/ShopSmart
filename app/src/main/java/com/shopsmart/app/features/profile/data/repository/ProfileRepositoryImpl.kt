package com.shopsmart.app.features.profile.data.repository


import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.profile.data.mappers.ProfileMappers.toDomain
import com.shopsmart.app.features.profile.data.remote.model.UpdateProfileRequestDto
import com.shopsmart.app.features.profile.domain.model.Profile
import com.shopsmart.app.features.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl : ProfileRepository {

    override suspend fun getProfile(): AppResult<Profile> {
        return try {
            val r = RetrofitClient.apiService.getProfileFull()
            if (r.isSuccessful) {
                val dto = r.body()?.data
                    ?: return AppResult.Failure(Exception("Profile data missing"))
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(
                    Exception(r.errorBody()?.string() ?: "Failed to load profile")
                )
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun updateProfile(
        fullName: String,
        phone: String?,
        avatarUrl: String?,
    ): AppResult<Profile> {
        return try {
            val r = RetrofitClient.apiService.updateProfile(
                UpdateProfileRequestDto(fullName, phone, avatarUrl)
            )
            if (r.isSuccessful) {
                val dto = r.body()?.data
                    ?: return AppResult.Failure(Exception("Profile data missing"))
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(
                    Exception(r.errorBody()?.string() ?: "Failed to update profile")
                )
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }
}