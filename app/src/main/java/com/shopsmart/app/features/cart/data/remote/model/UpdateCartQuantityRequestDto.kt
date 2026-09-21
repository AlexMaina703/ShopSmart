package com.shopsmart.app.features.cart.data.remote.model
import com.google.gson.annotations.SerializedName



data class UpdateCartQuantityRequestDto(
    @SerializedName("quantity") val quantity: Int,
)