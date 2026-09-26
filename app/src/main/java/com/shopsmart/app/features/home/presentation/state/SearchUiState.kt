package com.shopsmart.app.features.home.presentation.state

import com.shopsmart.app.features.home.domain.model.Product

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val results: List<Product> = emptyList(),
) {
    val showEmptyPrompt: Boolean get() = query.isBlank() && results.isEmpty()
    val showNoResults: Boolean get() = query.isNotBlank() && !isLoading && results.isEmpty() && errorMessage == null
}