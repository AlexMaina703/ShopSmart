package com.shopsmart.app.features.order.data.remote.model

import com.google.gson.annotations.SerializedName

data class OrderItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("productName") val productName: String,
    @SerializedName("productImage") val productImage: String? = null,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("color") val color: String? = null,
    @SerializedName("storage") val storage: String? = null,
)