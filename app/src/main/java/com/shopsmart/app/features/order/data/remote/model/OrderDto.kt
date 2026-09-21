package com.shopsmart.app.features.order.data.remote.model

import com.google.gson.annotations.SerializedName


data class OrderDto(
    @SerializedName("id") val id: String,
    @SerializedName("orderNumber") val orderNumber: String,
    @SerializedName("status") val status: String,
    @SerializedName("items") val items: List<OrderItemDto> = emptyList(),
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("shipping") val shipping: Double,
    @SerializedName("total") val total: Double,
    @SerializedName("shippingAddress") val shippingAddress: AddressDto? = null,
    @SerializedName("paymentMethod") val paymentMethod: PaymentMethodDto? = null,
    @SerializedName("tracking") val tracking: List<TrackingEventDto> = emptyList(), // 👈 NEW
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,
)