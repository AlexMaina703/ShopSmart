package com.shopsmart.app.features.cart.data.remote.model
import com.google.gson.annotations.SerializedName

data class CartDto(
    @SerializedName("items") val items: List<CartItemDto> = emptyList(),
    @SerializedName("subtotal") val subtotal: Double = 0.0,
    @SerializedName("shipping") val shipping: Double = 0.0,
    @SerializedName("total") val total: Double = 0.0,
    @SerializedName("itemCount") val itemCount: Int = 0,
)