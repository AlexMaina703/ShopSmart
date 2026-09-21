package com.shopsmart.app.features.cart.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.domain.model.Cart
import com.shopsmart.app.features.cart.domain.repository.CartRepository

class GetCartUseCase(private val repo: CartRepository) {
    suspend operator fun invoke(): AppResult<Cart> = repo.getCart()
}