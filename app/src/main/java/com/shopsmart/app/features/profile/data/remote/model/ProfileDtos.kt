package com.shopsmart.app.features.profile.data.remote.model

import com.google.gson.annotations.SerializedName
import com.shopsmart.app.features.order.data.remote.model.AddressDto
import com.shopsmart.app.features.order.data.remote.model.PaymentMethodDto

data class ProfileResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("emailVerified") val emailVerified: Boolean = false,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("addresses") val addresses: List<AddressDto> = emptyList(),
    @SerializedName("paymentMethods") val paymentMethods: List<PaymentMethodDto> = emptyList(),
)

data class UpdateProfileRequestDto(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
)