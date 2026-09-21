package com.shopsmart.app.features.cart.data.remote.model
import com.google.gson.annotations.SerializedName


data class AddToCartRequestDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("color") val color: String? = null,
    @SerializedName("storage") val storage: String? = null,
)