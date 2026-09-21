package com.shopsmart.app.features.cart.domain.repository


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.domain.model.Cart

interface CartRepository {
    suspend fun getCart(): AppResult<Cart>
    suspend fun addToCart(
        productId: String,
        quantity: Int,
        color: String? = null,
        storage: String? = null,
    ): AppResult<Cart>
    suspend fun updateQuantity(itemId: String, quantity: Int): AppResult<Cart>
    suspend fun removeItem(itemId: String): AppResult<Cart>
    suspend fun clearCart(): AppResult<Unit>
}