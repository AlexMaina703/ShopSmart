package com.shopsmart.app.features.wishlist.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.wishlist.domain.model.WishlistItem
import com.shopsmart.app.features.wishlist.domain.repository.WishlistRepository

class GetWishlistUseCase(private val repo: WishlistRepository) {
    suspend operator fun invoke(): AppResult<List<WishlistItem>> = repo.getWishlist()
}