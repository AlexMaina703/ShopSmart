package com.shopsmart.app.features.cart.data.repository


import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.data.mappers.CartMappers.toDomain
import com.shopsmart.app.features.cart.data.remote.model.AddToCartRequestDto
import com.shopsmart.app.features.cart.data.remote.model.UpdateCartQuantityRequestDto
import com.shopsmart.app.features.cart.domain.model.Cart
import com.shopsmart.app.features.cart.domain.repository.CartRepository

class CartRepositoryImpl : CartRepository {

    override suspend fun getCart(): AppResult<Cart> {
        return try {
            val response = RetrofitClient.apiService.getCart()
            if (response.isSuccessful) {
                val dto = response.body()?.data ?: return AppResult.Success(Cart.EMPTY)
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(response.errorBody()?.string() ?: "Failed to load cart"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun addToCart(
        productId: String,
        quantity: Int,
        color: String?,
        storage: String?,
    ): AppResult<Cart> {
        return try {
            val response = RetrofitClient.apiService.addToCart(
                AddToCartRequestDto(productId, quantity, color, storage)
            )
            if (response.isSuccessful) {
                val dto = response.body()?.data ?: return AppResult.Success(Cart.EMPTY)
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(response.errorBody()?.string() ?: "Failed to add to cart"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }


    override suspend fun updateQuantity(itemId: String, quantity: Int): AppResult<Cart> {
        return try{
            val response = RetrofitClient.apiService.updateCartItem(
                itemId, UpdateCartQuantityRequestDto(quantity)
            )

            if (response.isSuccessful){
                val dto = response.body()?.data ?: return AppResult.Success(Cart.EMPTY)

                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(response.errorBody()?.string() ?: "Failed to update cart item"))
            }


        }catch (e: Exception){
            AppResult.Failure(e)
        }
        }


    override suspend fun removeItem(itemId: String): AppResult<Cart> {
        return try {
            val response = RetrofitClient.apiService.removeCartItem(itemId)
            if (response.isSuccessful) {
                val dto = response.body()?.data ?: return AppResult.Success(Cart.EMPTY)
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(response.errorBody()?.string() ?: "Failed to remove item"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun clearCart(): AppResult<Unit> {
        return try {
            val response = RetrofitClient.apiService.clearCart()
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                AppResult.Failure(Exception(response.errorBody()?.string() ?: "Failed to clear cart"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    }





    

