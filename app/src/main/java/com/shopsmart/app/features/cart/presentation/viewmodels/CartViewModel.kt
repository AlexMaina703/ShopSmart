package com.shopsmart.app.features.cart.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.domain.usecase.*
import com.shopsmart.app.features.cart.presentation.state.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init { loadCart() }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val response = getCartUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, cart = response.data)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = response.exception.message ?: "Failed to load cart")
                }
            }
        }
    }

    fun increment(itemId: String, current: Int) {
        val max = 99
        if (current >= max) return
        updateQuantity(itemId, current + 1)
    }

    fun decrement(itemId: String, current: Int) {
        if (current <= 1) return
        updateQuantity(itemId, current - 1)
    }

    private fun updateQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(updatingItemId = itemId) }
            when (val r = updateCartItemUseCase(itemId, quantity)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(updatingItemId = null, cart = r.data)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        updatingItemId = null,
                        toastMessage = r.exception.message ?: "Failed to update",
                    )
                }
            }
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(updatingItemId = itemId) }
            when (val r = removeCartItemUseCase(itemId)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(updatingItemId = null, cart = r.data, toastMessage = "Item removed")
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        updatingItemId = null,
                        toastMessage = r.exception.message ?: "Failed to remove",
                    )
                }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            when (val r = clearCartUseCase()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(cart = com.shopsmart.app.features.cart.domain.model.Cart.EMPTY) }
                    loadCart()
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(toastMessage = r.exception.message ?: "Failed to clear cart")
                }
            }
        }
    }

    fun consumeToast() = _uiState.update { it.copy(toastMessage = null) }
}