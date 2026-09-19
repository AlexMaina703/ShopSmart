package com.shopsmart.app.features.home.presentation.state


import com.shopsmart.app.features.home.domain.model.Product

data class ProductDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val product: Product? = null,
    val relatedProducts: List<Product> = emptyList(),
    val selectedImageIndex: Int = 0,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val isAddingToCart: Boolean = false,
    val toastMessage: String? = null,
    val cartItemCount: Int = 0,
)