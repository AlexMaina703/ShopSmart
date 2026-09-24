package com.shopsmart.app.features.notification.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.notification.domain.usecase.*
import com.shopsmart.app.features.notification.presentation.state.NotificationsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markReadUseCase: MarkNotificationReadUseCase,
    private val markAllReadUseCase: MarkAllNotificationsReadUseCase,
    private val deleteUseCase: DeleteNotificationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val r = getNotificationsUseCase()) {
                is AppResult.Success -> {
                    val unread = r.data.count { !it.read }
                    _uiState.update {
                        it.copy(isLoading = false, notifications = r.data, unreadCount = unread)
                    }
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = r.exception.message ?: "Failed to load notifications",
                    )
                }
            }
        }
    }

    fun markRead(id: String) {
        viewModelScope.launch {
            when (markReadUseCase(id)) {
                is AppResult.Success -> _uiState.update { state ->
                    state.copy(
                        notifications = state.notifications.map {
                            if (it.id == id) it.copy(read = true) else it
                        },
                        unreadCount = (state.unreadCount - 1).coerceAtLeast(0),
                    )
                }
                is AppResult.Failure -> Unit
            }
        }
    }

    fun markAllRead() {
        viewModelScope.launch {
            when (markAllReadUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        notifications = it.notifications.map { n -> n.copy(read = true) },
                        unreadCount = 0,
                        toastMessage = "All marked as read",
                    )
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(toastMessage = "Failed to mark all read")
                }
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            val wasUnread = _uiState.value.notifications.firstOrNull { it.id == id }?.read == false
            when (deleteUseCase(id)) {
                is AppResult.Success -> _uiState.update { state ->
                    state.copy(
                        notifications = state.notifications.filterNot { it.id == id },
                        unreadCount = if (wasUnread)
                            (state.unreadCount - 1).coerceAtLeast(0)
                        else state.unreadCount,
                        toastMessage = "Notification deleted",
                    )
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(toastMessage = "Failed to delete")
                }
            }
        }
    }

    fun consumeToast() = _uiState.update { it.copy(toastMessage = null) }
}