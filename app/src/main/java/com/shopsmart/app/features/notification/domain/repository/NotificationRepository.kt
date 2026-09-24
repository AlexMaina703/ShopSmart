package com.shopsmart.app.features.notification.domain.repository

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.notification.domain.model.AppNotification

interface NotificationRepository {
    suspend fun getNotifications(): AppResult<List<AppNotification>>
    suspend fun getUnreadCount(): AppResult<Int>
    suspend fun markAsRead(id: String): AppResult<Unit>
    suspend fun markAllAsRead(): AppResult<Unit>
    suspend fun delete(id: String): AppResult<Unit>
}