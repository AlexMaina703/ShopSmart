package com.shopsmart.app.features.notification.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.notification.domain.repository.NotificationRepository

class GetUnreadCountUseCase(private val repo: NotificationRepository) {
    suspend operator fun invoke(): AppResult<Int> = repo.getUnreadCount()
}