package com.shopsmart.app.features.order.data.remote.model

import com.google.gson.annotations.SerializedName

data class TrackingEventDto(
    @SerializedName("id") val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("location") val location: String? = null,
    @SerializedName("note") val note: String? = null,
    @SerializedName("timestamp") val timestamp: String,
)