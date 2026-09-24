package com.shopsmart.app.features.wishlist.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.wishlist.domain.repository.WishlistRepository

class AddToWishlistUseCase(private val repo: WishlistRepository) {
    suspend operator fun invoke(productId: String): AppResult<Unit> =
        repo.addToWishlist(productId)
}