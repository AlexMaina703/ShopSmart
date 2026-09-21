package com.shopsmart.app.features.home.presentation.state

import com.shopsmart.app.features.home.domain.model.Category
import com.shopsmart.app.features.home.domain.model.Product

data class CategoriesUiState(
    val isLoadingCategories: Boolean = true,
    val categoriesError: String? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,

    val isLoadingProducts: Boolean = false,
    val productsError: String? = null,
    val products: List<Product> = emptyList(),
) {
    val selectedCategory: Category?
        get() = categories.firstOrNull { it.id == selectedCategoryId }
}