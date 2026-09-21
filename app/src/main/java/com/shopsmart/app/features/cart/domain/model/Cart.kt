package com.shopsmart.app.features.cart.domain.model

data class Cart(
    val items: List<CartItem>,
    val subtotal: Double,
    val shipping: Double,
    val total: Double,
    val itemCount: Int,
) {
    companion object {
        val EMPTY = Cart(emptyList(), 0.0, 0.0, 0.0, 0)
    }

    val isEmpty: Boolean get() = items.isEmpty()
}