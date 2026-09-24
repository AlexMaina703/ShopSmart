package com.shopsmart.app.features.settings.presentation.state

data class SettingsUiState(
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val language: String = "en",
)