package com.shopsmart.app.features.order.data.remote.model

import com.google.gson.annotations.SerializedName

data class AddressDto(
    @SerializedName("id") val id: String,
    @SerializedName("label") val label: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,
    @SerializedName("city") val city: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("isDefault") val isDefault: Boolean = false,
)