package com.shopsmart.app.features.order.presentation.state

import com.shopsmart.app.features.order.domain.model.Address

data class AddressesUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val addresses: List<Address> = emptyList(),
    val deletingId: String? = null,
    val toastMessage: String? = null,
)