package com.shopsmart.app.features.wishlist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.wishlist.domain.usecase.GetWishlistUseCase
import com.shopsmart.app.features.wishlist.domain.usecase.RemoveFromWishlistUseCase
import com.shopsmart.app.features.wishlist.presentation.state.WishlistUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val r = getWishlistUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, items = r.data)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = r.exception.message ?: "Failed to load wishlist",
                    )
                }
            }
        }
    }

    fun remove(itemId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(removingItemId = itemId) }
            when (val r = removeFromWishlistUseCase(itemId)) {
                is AppResult.Success -> {
                    // Optimistic remove — drop locally, then confirm
                    _uiState.update {
                        it.copy(
                            removingItemId = null,
                            items = it.items.filterNot { item -> item.id == itemId },
                            toastMessage = "Removed from wishlist",
                        )
                    }
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        removingItemId = null,
                        toastMessage = r.exception.message ?: "Failed to remove",
                    )
                }
            }
        }
    }

    fun consumeToast() = _uiState.update { it.copy(toastMessage = null) }
}