package com.shopsmart.app.features.profile.presentation.state

data class EditProfileUiState(
    val fullName: String = "",
    val phone: String = "",
    val avatarUrl: String = "",

    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val savedSuccessfully: Boolean = false,
) {
    val isValid: Boolean get() = fullName.isNotBlank()
}