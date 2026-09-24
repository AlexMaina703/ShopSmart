package com.shopsmart.app.features.wishlist.domain.model


data class WishlistProduct(
    val id: String,
    val name: String,
    val price: Double,
    val images: List<String>,
    val rating: Double,
) {
    val primaryImage: String? get() = images.firstOrNull()
}