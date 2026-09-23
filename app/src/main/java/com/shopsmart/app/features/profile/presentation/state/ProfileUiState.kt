package com.shopsmart.app.features.profile.presentation.state

import com.shopsmart.app.features.profile.domain.model.Profile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val profile: Profile? = null,
)