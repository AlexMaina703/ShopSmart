package com.shopsmart.app.features.cart.data.remote.model

import com.google.gson.annotations.SerializedName

data class CartProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("images") val images: List<String> = emptyList(),
)
