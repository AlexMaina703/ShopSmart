package com.shopsmart.app.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.usecase.GetProductByIdUseCase
import com.shopsmart.app.features.home.domain.usecase.GetRelatedProductsUseCase
import com.shopsmart.app.features.home.presentation.state.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getRelatedProductsUseCase: GetRelatedProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private var productId: String? = null

    fun load(productId: String) {
        this.productId = productId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val productResult = getProductByIdUseCase(productId)
            if (productResult is AppResult.Failure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = productResult.exception.message ?: "Failed to load product",
                    )
                }
                return@launch
            }

            val product = (productResult as AppResult.Success).data
            _uiState.update {
                it.copy(
                    isLoading = false,
                    product = product,
                    quantity = 1,
                )
            }

            // Related products load in the background
            val related = getRelatedProductsUseCase(productId)
            if (related is AppResult.Success) {
                _uiState.update { it.copy(relatedProducts = related.data) }
            }
        }
    }

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

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
        // TODO: call wishlist repository
    }

    fun addToCart() {
        val p = _uiState.value.product ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }

            // TODO: wire to CartRepository — for now, simulate
            kotlinx.coroutines.delay(400)

            _uiState.update {
                it.copy(
                    isAddingToCart = false,
                    toastMessage = "${p.name} added to cart",
                    cartItemCount = it.cartItemCount + it.quantity,
                )
            }
        }
    }

    fun buyNow() {
        // TODO: navigate to Checkout after adding to cart
        _uiState.update { it.copy(toastMessage = "Buy Now flow coming soon") }
    }

    fun consumeToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}