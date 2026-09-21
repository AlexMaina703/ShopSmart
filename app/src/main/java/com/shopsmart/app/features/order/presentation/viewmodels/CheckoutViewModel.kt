package com.shopsmart.app.features.order.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.cart.domain.usecase.ClearCartUseCase
import com.shopsmart.app.features.order.domain.usecase.*
import com.shopsmart.app.features.order.presentation.state.CheckoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val clearCartUseCase: ClearCartUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val addresses = getAddressesUseCase()
            val payments = getPaymentMethodsUseCase()

            val addressList = (addresses as? AppResult.Success)?.data.orEmpty()
            val paymentList = (payments as? AppResult.Success)?.data.orEmpty()

            // Auto-select defaults
            val defaultAddress = addressList.firstOrNull { it.isDefault }?.id
                ?: addressList.firstOrNull()?.id
            val defaultPayment = paymentList.firstOrNull { it.isDefault }?.id
                ?: paymentList.firstOrNull()?.id

            val error = when {
                addresses is AppResult.Failure -> addresses.exception.message
                payments is AppResult.Failure -> payments.exception.message
                else -> null
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    addresses = addressList,
                    paymentMethods = paymentList,
                    selectedAddressId = defaultAddress,
                    selectedPaymentId = defaultPayment,
                    errorMessage = if (addressList.isEmpty() && paymentList.isEmpty()) error else null,
                )
            }
        }
    }

    fun selectAddress(id: String) = _uiState.update { it.copy(selectedAddressId = id) }
    fun selectPayment(id: String) = _uiState.update { it.copy(selectedPaymentId = id) }

    fun placeOrder() {
        val addressId = _uiState.value.selectedAddressId ?: return
        val paymentId = _uiState.value.selectedPaymentId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isPlacingOrder = true) }
            when (val r = placeOrderUseCase(addressId, paymentId)) {
                is AppResult.Success -> {
                    // Clear the cart on the server after a successful order
                    clearCartUseCase()
                    _uiState.update {
                        it.copy(isPlacingOrder = false, placedOrderId = r.data.id)
                    }
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isPlacingOrder = false,
                        toastMessage = r.exception.message ?: "Failed to place order",
                    )
                }
            }
        }
    }

    fun consumeToast() = _uiState.update { it.copy(toastMessage = null) }
    fun consumeOrderId() = _uiState.update { it.copy(placedOrderId = null) }
}