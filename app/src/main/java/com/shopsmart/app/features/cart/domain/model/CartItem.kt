package com.shopsmart.app.features.cart.domain.model

data class CartItem(
    val id: String,
    val product: CartProduct,
    val quantity: Int,
    val selectedColor: String?,
    val selectedStorage: String?,
) {
    val lineTotal: Double get() = product.price * quantity
}