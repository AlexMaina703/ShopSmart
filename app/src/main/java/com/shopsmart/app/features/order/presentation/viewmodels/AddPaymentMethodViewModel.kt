package com.shopsmart.app.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.usecase.AddPaymentMethodUseCase
import com.shopsmart.app.features.order.presentation.state.AddPaymentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddPaymentMethodViewModel(
    private val addPaymentMethodUseCase: AddPaymentMethodUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPaymentUiState())
    val uiState: StateFlow<AddPaymentUiState> = _uiState.asStateFlow()

    fun setCardHolder(v: String) = _uiState.update { it.copy(cardHolder = v) }

    fun setCardNumber(v: String) {
        // Keep digits only, group by 4
        val digits = v.filter { it.isDigit() }.take(19)
        val grouped = digits.chunked(4).joinToString(" ")
        val type = when {
            digits.startsWith("4") -> "VISA"
            digits.startsWith("5") -> "MASTERCARD"
            digits.startsWith("3") -> "AMEX"
            else -> "CARD"
        }
        _uiState.update { it.copy(cardNumber = grouped, cardType = type) }
    }

    fun setExpiryMonth(v: String) = _uiState.update { it.copy(expiryMonth = v.filter { c -> c.isDigit() }.take(2)) }
    fun setExpiryYear(v: String) = _uiState.update { it.copy(expiryYear = v.filter { c -> c.isDigit() }.take(4)) }
    fun setCvv(v: String) = _uiState.update { it.copy(cvv = v.filter { c -> c.isDigit() }.take(4)) }
    fun setDefault(v: Boolean) = _uiState.update { it.copy(isDefault = v) }

    fun save() {
        val s = _uiState.value
        if (!s.isValid || s.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            when (val r = addPaymentMethodUseCase(
                cardNumber = s.cardNumber.replace(" ", ""),
                cardType = s.cardType,
                expiryMonth = s.expiryMonth.toInt(),
                expiryYear = s.expiryYear.toInt(),
                cvv = s.cvv,
                isDefault = s.isDefault,
            )) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isSaving = false, savedSuccessfully = true)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = r.exception.message ?: "Failed to save card",
                    )
                }
            }
        }
    }
}