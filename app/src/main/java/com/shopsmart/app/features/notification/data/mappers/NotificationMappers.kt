package com.shopsmart.app.features.notification.data.mappers

import com.shopsmart.app.features.notification.data.remote.model.NotificationDto
import com.shopsmart.app.features.notification.domain.model.AppNotification

object NotificationMappers {
    fun NotificationDto.toDomain() = AppNotification(
        id = id, type = type, title = title, message = message,
        read = read, createdAt = createdAt,
    )
}