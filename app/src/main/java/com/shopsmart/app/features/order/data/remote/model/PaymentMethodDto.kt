package com.shopsmart.app.features.order.data.remote.model

import com.google.gson.annotations.SerializedName

data class PaymentMethodDto(
    @SerializedName("id") val id: String,
    @SerializedName("cardType") val cardType: String,
    @SerializedName("lastFour") val lastFour: String,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("expiryMonth") val expiryMonth: Int,
    @SerializedName("expiryYear") val expiryYear: Int,
    @SerializedName("isDefault") val isDefault: Boolean = false,
    )