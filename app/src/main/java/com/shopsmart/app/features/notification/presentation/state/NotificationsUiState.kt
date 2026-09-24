package com.shopsmart.app.features.notification.presentation.state


import com.shopsmart.app.features.notification.domain.model.AppNotification

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val notifications: List<AppNotification> = emptyList(),
    val unreadCount: Int = 0,
    val toastMessage: String? = null,
)