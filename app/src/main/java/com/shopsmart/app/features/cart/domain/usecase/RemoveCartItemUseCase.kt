package com.shopsmart.app.features.cart.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.domain.model.Cart
import com.shopsmart.app.features.cart.domain.repository.CartRepository


class RemoveCartItemUseCase(private val repo: CartRepository) {
    suspend operator fun invoke(itemId: String): AppResult<Cart> = repo.removeItem(itemId)
}