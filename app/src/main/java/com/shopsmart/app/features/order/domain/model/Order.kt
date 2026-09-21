package com.shopsmart.app.features.order.domain.model

data class Order(
    val id: String,
    val orderNumber: String,
    val status: String,
    val items: List<OrderItem>,
    val subtotal: Double,
    val shipping: Double,
    val total: Double,
    val shippingAddress: Address?,
    val paymentMethod: PaymentMethod?,
    val tracking: List<TrackingEvent>,   // 👈 NEW
    val createdAt: String?,
)