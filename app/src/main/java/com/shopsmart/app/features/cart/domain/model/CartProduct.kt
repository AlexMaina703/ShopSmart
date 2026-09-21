package com.shopsmart.app.features.cart.domain.model

data class CartProduct(
    val id: String,
    val name: String,
    val price: Double,
    val images: List<String>,
) {
    val primaryImage: String? get() = images.firstOrNull()
}
