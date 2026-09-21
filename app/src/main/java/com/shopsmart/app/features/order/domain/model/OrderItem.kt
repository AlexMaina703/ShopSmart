package com.shopsmart.app.features.order.domain.model

data class OrderItem(
    val id: String,
    val productId: String,
    val productName: String,
    val productImage: String?,
    val quantity: Int,
    val price: Double,
    val color: String?,
    val storage: String?,
)