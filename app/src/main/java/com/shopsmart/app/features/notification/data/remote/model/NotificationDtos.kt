package com.shopsmart.app.features.notification.data.remote.model

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("read") val read: Boolean = false,
    @SerializedName("createdAt") val createdAt: String? = null,
)

data class UnreadCountDto(
    @SerializedName("count") val count: Int = 0,
)