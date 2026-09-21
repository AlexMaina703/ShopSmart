package com.shopsmart.app.features.order.domain.model


data class TrackingEvent(
    val id: String,
    val status: String,
    val location: String?,
    val note: String?,
    val timestamp: String,
)