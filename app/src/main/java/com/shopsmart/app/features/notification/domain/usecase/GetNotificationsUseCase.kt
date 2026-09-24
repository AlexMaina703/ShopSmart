package com.shopsmart.app.features.notification.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.notification.domain.model.AppNotification
import com.shopsmart.app.features.notification.domain.repository.NotificationRepository

class GetNotificationsUseCase(private val repo: NotificationRepository) {
    suspend operator fun invoke(): AppResult<List<AppNotification>> = repo.getNotifications()
}