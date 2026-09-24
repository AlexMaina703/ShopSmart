package com.shopsmart.app.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.usecase.DeletePaymentMethodUseCase
import com.shopsmart.app.features.order.domain.usecase.GetPaymentMethodsUseCase
import com.shopsmart.app.features.order.presentation.state.PaymentMethodsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentMethodsViewModel(
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val deletePaymentMethodUseCase: DeletePaymentMethodUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentMethodsUiState())
    val uiState: StateFlow<PaymentMethodsUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val r = getPaymentMethodsUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, methods = r.data)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = r.exception.message ?: "Failed to load cards",
                    )
                }
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(deletingId = id) }
            when (val r = deletePaymentMethodUseCase(id)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        deletingId = null,
                        methods = it.methods.filterNot { m -> m.id == id },
                        toastMessage = "Card removed",
                    )
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        deletingId = null,
                        toastMessage = r.exception.message ?: "Failed to remove",
                    )
                }
            }
        }
    }

    fun consumeToast() = _uiState.update { it.copy(toastMessage = null) }
}