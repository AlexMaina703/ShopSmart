package com.shopsmart.app.features.wishlist.data.model

import com.google.gson.annotations.SerializedName

data class WishlistItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("product") val product: WishlistProductDto,
    @SerializedName("addedAt") val addedAt: String? = null,
)