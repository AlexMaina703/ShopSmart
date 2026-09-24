package com.shopsmart.app.features.wishlist.domain.model

data class WishlistItem(
    val id: String,
    val product: WishlistProduct,
    val addedAt: String?,
)