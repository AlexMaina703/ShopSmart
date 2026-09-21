package com.shopsmart.app.features.home.presentation.state

import com.shopsmart.app.features.home.domain.model.Product

data class ProductListUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val products: List<Product> = emptyList(),
    val categoryName: String = "",
    val sortBy: String? = null,
)