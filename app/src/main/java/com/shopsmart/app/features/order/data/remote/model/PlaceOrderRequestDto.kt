package com.shopsmart.app.features.order.data.remote.model

import com.google.gson.annotations.SerializedName

data class PlaceOrderRequestDto(
    @SerializedName("addressId") val addressId: String,
    @SerializedName("paymentMethodId") val paymentMethodId: String,
    @SerializedName("shippingMethod") val shippingMethod: String = "standard",
)