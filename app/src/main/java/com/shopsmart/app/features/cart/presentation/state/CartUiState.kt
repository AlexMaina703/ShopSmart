package com.shopsmart.app.features.cart.presentation.state

import com.shopsmart.app.features.cart.domain.model.Cart

data class CartUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val cart: Cart = Cart.EMPTY,
    val updatingItemId: String? = null,
    val toastMessage: String? = null,
)