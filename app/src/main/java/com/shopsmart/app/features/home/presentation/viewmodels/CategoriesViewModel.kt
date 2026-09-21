package com.shopsmart.app.features.home.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.Category
import com.shopsmart.app.features.home.domain.usecase.GetCategoriesUseCase
import com.shopsmart.app.features.home.domain.usecase.GetProductsUseCase
import com.shopsmart.app.features.home.presentation.state.CategoriesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCategories = true, categoriesError = null) }

            when (val result = getCategoriesUseCase()) {
                is AppResult.Success -> {
                    val list = result.data
                    val firstId = list.firstOrNull()?.id
                    _uiState.update {
                        it.copy(
                            isLoadingCategories = false,
                            categories = list,
                            selectedCategoryId = firstId,
                        )
                    }
                    firstId?.let { loadProductsForCategory(it) }
                }
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoadingCategories = false,
                            categoriesError = result.exception.message
                                ?: "Failed to load categories",
                        )
                    }
                }
            }
        }
    }

    fun selectCategory(category: Category) {
        if (_uiState.value.selectedCategoryId == category.id) return
        _uiState.update { it.copy(selectedCategoryId = category.id) }
        loadProductsForCategory(category.id)
    }

    private fun loadProductsForCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingProducts = true,
                    productsError = null,
                    products = emptyList(),
                )
            }

            when (val result = getProductsUseCase(categoryId = categoryId, pageSize = 20)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingProducts = false,
                            products = result.data.products,
                        )
                    }
                }
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoadingProducts = false,
                            productsError = result.exception.message
                                ?: "Failed to load products",
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        if (_uiState.value.categories.isEmpty()) loadCategories()
        else _uiState.value.selectedCategoryId?.let { loadProductsForCategory(it) }
    }
}