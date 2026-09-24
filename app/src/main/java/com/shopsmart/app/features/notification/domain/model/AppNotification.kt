package com.shopsmart.app.features.notification.domain.model

data class AppNotification(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val read: Boolean,
    val createdAt: String?,
)