package com.shopsmart.app.features.cart.data.remote.model
import com.google.gson.annotations.SerializedName

data class CartItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("product") val product: CartProductDto,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("selectedColor") val selectedColor: String? = null,
    @SerializedName("selectedStorage") val selectedStorage: String? = null,
)