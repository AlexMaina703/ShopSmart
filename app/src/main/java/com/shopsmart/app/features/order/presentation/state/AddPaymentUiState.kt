package com.shopsmart.app.features.order.presentation.state

data class AddPaymentUiState(
    val cardHolder: String = "",
    val cardNumber: String = "",
    val cardType: String = "VISA",
    val expiryMonth: String = "",
    val expiryYear: String = "",
    val cvv: String = "",
    val isDefault: Boolean = false,

    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val savedSuccessfully: Boolean = false,
) {
    val isValid: Boolean
        get() = cardHolder.isNotBlank() &&
                cardNumber.replace(" ", "").length in 13..19 &&
                expiryMonth.toIntOrNull() in 1..12 &&
                expiryYear.toIntOrNull() in 2000..2100 &&
                cvv.length in 3..4
}