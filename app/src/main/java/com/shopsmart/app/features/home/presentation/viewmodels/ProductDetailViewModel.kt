package com.shopsmart.app.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.domain.usecase.AddToCartUseCase
import com.shopsmart.app.features.home.domain.usecase.GetProductByIdUseCase
import com.shopsmart.app.features.home.domain.usecase.GetRelatedProductsUseCase
import com.shopsmart.app.features.home.presentation.state.ProductDetailUiState
import com.shopsmart.app.features.wishlist.domain.usecase.AddToWishlistUseCase
import com.shopsmart.app.features.wishlist.domain.usecase.GetWishlistUseCase
import com.shopsmart.app.features.wishlist.domain.usecase.RemoveFromWishlistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getRelatedProductsUseCase: GetRelatedProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private var productId: String? = null

    /**
     * The wishlist entry ID for the currently displayed product.
     * The server uses this to delete — NOT the product ID.
     * Null when the product is not in the wishlist.
     */
    private var wishlistItemId: String? = null

    // ------------------------------------------------------------------
    //  LOAD
    // ------------------------------------------------------------------
    fun load(productId: String) {
        this.productId = productId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // 1. Product
            when (val productResult = getProductByIdUseCase(productId)) {
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = productResult.exception.message
                                ?: "Failed to load product",
                        )
                    }
                    return@launch
                }
                is AppResult.Success -> {
                    val product = productResult.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            product = product,
                            quantity = 1,
                        )
                    }
                }
            }

            // 2. Related products (background — don't fail the screen if this errors)
            when (val related = getRelatedProductsUseCase(productId)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(relatedProducts = related.data)
                }
                is AppResult.Failure -> Unit
            }

            // 3. Is this product already in the wishlist?
            when (val wl = getWishlistUseCase()) {
                is AppResult.Success -> {
                    wishlistItemId = wl.data
                        .firstOrNull { it.product.id == productId }
                        ?.id
                    _uiState.update { it.copy(isFavorite = wishlistItemId != null) }
                }
                is AppResult.Failure -> Unit // non-fatal
            }
        }
    }

    // ------------------------------------------------------------------
    //  IMAGE + QUANTITY
    // ------------------------------------------------------------------
    fun selectImage(index: Int) {
        _uiState.update { it.copy(selectedImageIndex = index) }
    }

    fun incrementQuantity() {
        val max = _uiState.value.product?.stock ?: Int.MAX_VALUE
        _uiState.update { it.copy(quantity = (it.quantity + 1).coerceAtMost(max)) }
    }

    fun decrementQuantity() {
        _uiState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(1)) }
    }

    // ------------------------------------------------------------------
    //  WISHLIST
    // ------------------------------------------------------------------
    fun toggleFavorite() {
        val pid = productId ?: return

        viewModelScope.launch {
            val existingItemId = wishlistItemId

            if (existingItemId != null) {
                // ----- REMOVE -----
                when (val r = removeFromWishlistUseCase(existingItemId)) {
                    is AppResult.Success -> {
                        wishlistItemId = null
                        _uiState.update {
                            it.copy(
                                isFavorite = false,
                                toastMessage = "Removed from wishlist",
                            )
                        }
                    }
                    is AppResult.Failure -> _uiState.update {
                        it.copy(
                            toastMessage = r.exception.message
                                ?: "Failed to remove from wishlist",
                        )
                    }
                }
            } else {
                // ----- ADD -----
                when (val r = addToWishlistUseCase(pid)) {
                    is AppResult.Success -> {
                        // Re-fetch wishlist to capture the new item ID
                        when (val wl = getWishlistUseCase()) {
                            is AppResult.Success -> {
                                wishlistItemId = wl.data
                                    .firstOrNull { it.product.id == pid }
                                    ?.id
                            }
                            is AppResult.Failure -> Unit
                        }
                        _uiState.update {
                            it.copy(
                                isFavorite = true,
                                toastMessage = "Added to wishlist",
                            )
                        }
                    }
                    is AppResult.Failure -> _uiState.update {
                        it.copy(
                            toastMessage = r.exception.message
                                ?: "Failed to add to wishlist",
                        )
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------
    //  CART
    // ------------------------------------------------------------------
    fun addToCart() {
        val p = _uiState.value.product ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }
            when (val r = addToCartUseCase(
                productId = p.id,
                quantity = _uiState.value.quantity,
            )) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isAddingToCart = false,
                        toastMessage = "${p.name} added to cart",
                        cartItemCount = r.data.itemCount,
                    )
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isAddingToCart = false,
                        toastMessage = r.exception.message ?: "Failed to add to cart",
                    )
                }
            }
        }
    }

    fun buyNow() {
        // TODO: add to cart + navigate to Checkout
        _uiState.update { it.copy(toastMessage = "Buy Now flow coming soon") }
    }

    // ------------------------------------------------------------------
    //  UI events
    // ------------------------------------------------------------------
    fun consumeToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}