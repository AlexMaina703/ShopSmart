package com.shopsmart.app.features.order.presentation.state


import com.shopsmart.app.features.order.domain.model.Address
import com.shopsmart.app.features.order.domain.model.PaymentMethod

data class CheckoutUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    val addresses: List<Address> = emptyList(),
    val paymentMethods: List<PaymentMethod> = emptyList(),

    val selectedAddressId: String? = null,
    val selectedPaymentId: String? = null,

    val isPlacingOrder: Boolean = false,
    val placedOrderId: String? = null,     // when non-null → navigate to OrderSuccess
    val toastMessage: String? = null,
) {
    val selectedAddress get() = addresses.firstOrNull { it.id == selectedAddressId }
    val selectedPayment get() = paymentMethods.firstOrNull { it.id == selectedPaymentId }
    val canPlaceOrder get() = selectedAddressId != null && selectedPaymentId != null && !isPlacingOrder
}