package com.shopsmart.app.features.notification.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.notification.domain.model.AppNotification
import com.shopsmart.app.features.notification.domain.repository.NotificationRepository

class MarkAllNotificationsReadUseCase(private val repo: NotificationRepository) {
    suspend operator fun invoke(): AppResult<Unit> = repo.markAllAsRead()
}