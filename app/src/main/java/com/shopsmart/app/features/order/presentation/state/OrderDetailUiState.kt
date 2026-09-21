package com.shopsmart.app.features.order.presentation.state

import com.shopsmart.app.features.order.domain.model.Order

data class OrderDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val order: Order? = null,
)