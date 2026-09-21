package com.shopsmart.app.features.cart.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.domain.model.Cart
import com.shopsmart.app.features.cart.domain.repository.CartRepository

class AddToCartUseCase(private val repo: CartRepository) {
    suspend operator fun invoke(
        productId: String,
        quantity: Int,
        color: String? = null,
        storage: String? = null,
    ): AppResult<Cart> = repo.addToCart(productId, quantity, color, storage)
}