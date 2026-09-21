package com.shopsmart.app.features.order.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.usecase.GetOrderByIdUseCase
import com.shopsmart.app.features.order.presentation.state.OrderDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    fun load(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val r = getOrderByIdUseCase(orderId)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, order = r.data)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = r.exception.message ?: "Failed to load order",
                    )
                }
            }
        }
    }
}