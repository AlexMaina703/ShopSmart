package com.shopsmart.app.features.wishlist.data.repository

import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.wishlist.data.mappers.WishlistMappers.toDomain
import com.shopsmart.app.features.wishlist.domain.model.WishlistItem
import com.shopsmart.app.features.wishlist.domain.repository.WishlistRepository

class WishlistRepositoryImpl : WishlistRepository {

    override suspend fun getWishlist(): AppResult<List<WishlistItem>> {
        return try {
            val r = RetrofitClient.apiService.getWishlist()
            if (r.isSuccessful) {
                AppResult.Success(r.body()?.data?.map { it.toDomain() }.orEmpty())
            } else {
                AppResult.Failure(
                    Exception(r.errorBody()?.string() ?: "Failed to load wishlist")
                )
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun addToWishlist(productId: String): AppResult<Unit> {
        return try {
            val r = RetrofitClient.apiService.addToWishlist(productId)
            if (r.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(
                Exception(r.errorBody()?.string() ?: "Failed to add to wishlist")
            )
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun removeFromWishlist(wishlistItemId: String): AppResult<Unit> {
        return try {
            val r = RetrofitClient.apiService.removeFromWishlist(wishlistItemId)
            if (r.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(
                Exception(r.errorBody()?.string() ?: "Failed to remove from wishlist")
            )
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }
}