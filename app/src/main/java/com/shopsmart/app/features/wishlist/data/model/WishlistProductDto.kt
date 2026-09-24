package com.shopsmart.app.features.wishlist.data.model


import com.google.gson.annotations.SerializedName

data class WishlistProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("images") val images: List<String> = emptyList(),
    @SerializedName("rating") val rating: Double = 0.0,
)
