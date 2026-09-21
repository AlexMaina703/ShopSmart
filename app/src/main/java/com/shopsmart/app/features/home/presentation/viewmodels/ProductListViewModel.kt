package com.shopsmart.app.features.home.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.usecase.GetProductsUseCase
import com.shopsmart.app.features.home.presentation.state.ProductListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var categoryId: String? = null

    fun load(categoryId: String, categoryName: String) {
        this.categoryId = categoryId
        _uiState.update { it.copy(categoryName = categoryName) }
        fetch()
    }

    fun setSort(sortBy: String?) {
        _uiState.update { it.copy(sortBy = sortBy) }
        fetch()
    }

    private fun fetch() {
        val cid = categoryId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getProductsUseCase(
                categoryId = cid,
                sortBy = _uiState.value.sortBy,
                pageSize = 40,
            )) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, products = result.data.products)
                    }
                }
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Failed to load products",
                        )
                    }
                }
            }
        }
    }
}