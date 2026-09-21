package com.shopsmart.app.features.order.data.remote.model


import com.google.gson.annotations.SerializedName

data class AddPaymentMethodRequestDto(
    @SerializedName("cardNumber") val cardNumber: String,
    @SerializedName("cardType") val cardType: String,
    @SerializedName("expiryMonth") val expiryMonth: Int,
    @SerializedName("expiryYear") val expiryYear: Int,
    @SerializedName("cvv") val cvv: String,
    @SerializedName("isDefault") val isDefault: Boolean = false,
)