package com.shopsmart.app.features.wishlist.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.wishlist.domain.repository.WishlistRepository


class RemoveFromWishlistUseCase(private val repo: WishlistRepository) {
    suspend operator fun invoke(wishlistItemId: String): AppResult<Unit> =
        repo.removeFromWishlist(wishlistItemId)
}