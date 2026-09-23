package com.shopsmart.app.features.profile.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.profile.domain.model.Profile
import com.shopsmart.app.features.profile.domain.usecase.UpdateProfileUseCase
import com.shopsmart.app.features.profile.presentation.state.EditProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    /** Call once when the screen loads with the current profile values. */
    fun seed(profile: Profile) {
        if (_uiState.value.fullName.isNotBlank()) return  // don't overwrite user edits
        _uiState.update {
            it.copy(
                fullName = profile.fullName,
                phone = profile.phone.orEmpty(),
                avatarUrl = profile.avatarUrl.orEmpty(),
            )
        }
    }

    fun setFullName(v: String) = _uiState.update { it.copy(fullName = v) }
    fun setPhone(v: String) = _uiState.update { it.copy(phone = v) }
    fun setAvatarUrl(v: String) = _uiState.update { it.copy(avatarUrl = v) }

    fun save() {
        val s = _uiState.value
        if (!s.isValid || s.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            when (val r = updateProfileUseCase(
                fullName = s.fullName.trim(),
                phone = s.phone.trim().takeIf { it.isNotBlank() },
                avatarUrl = s.avatarUrl.trim().takeIf { it.isNotBlank() },
            )) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isSaving = false, savedSuccessfully = true)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = r.exception.message ?: "Failed to save profile",
                    )
                }
            }
        }
    }
}