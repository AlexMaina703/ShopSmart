package com.shopsmart.app.features.order.presentation.state

import com.shopsmart.app.features.order.domain.model.PaymentMethod

data class PaymentMethodsUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val methods: List<PaymentMethod> = emptyList(),
    val deletingId: String? = null,
    val toastMessage: String? = null,
)