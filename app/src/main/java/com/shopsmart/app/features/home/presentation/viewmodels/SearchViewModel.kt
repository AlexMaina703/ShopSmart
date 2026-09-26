package com.shopsmart.app.features.home.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.usecase.GetProductsUseCase
import com.shopsmart.app.features.home.presentation.state.SearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(350)
                .distinctUntilChanged()
                .collect { q ->
                    if (q.isBlank()) {
                        _uiState.update {
                            it.copy(isLoading = false, results = emptyList(), errorMessage = null)
                        }
                    } else {
                        performSearch(q)
                    }
                }
        }
    }

    fun onQueryChange(q: String) {
        queryFlow.value = q
        _uiState.update { it.copy(query = q) }
    }

    fun clear() {
        queryFlow.value = ""
        _uiState.update {
            it.copy(query = "", results = emptyList(), errorMessage = null, isLoading = false)
        }
    }

    private suspend fun performSearch(q: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        when (val r = getProductsUseCase(search = q, pageSize = 40)) {
            is AppResult.Success -> _uiState.update {
                it.copy(isLoading = false, results = r.data.products)
            }
            is AppResult.Failure -> _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = r.exception.message ?: "Search failed",
                )
            }
        }
    }
}