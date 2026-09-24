package com.shopsmart.app.features.wishlist.domain.repository

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.wishlist.domain.model.WishlistItem

interface WishlistRepository {
    suspend fun getWishlist(): AppResult<List<WishlistItem>>
    suspend fun addToWishlist(productId: String): AppResult<Unit>
    suspend fun removeFromWishlist(wishlistItemId: String): AppResult<Unit>
}