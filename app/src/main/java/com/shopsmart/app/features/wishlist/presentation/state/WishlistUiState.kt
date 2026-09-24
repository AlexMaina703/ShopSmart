package com.shopsmart.app.features.wishlist.presentation.state


import com.shopsmart.app.features.wishlist.domain.model.WishlistItem

data class WishlistUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val items: List<WishlistItem> = emptyList(),
    val removingItemId: String? = null,
    val toastMessage: String? = null,
) {
    val isEmpty: Boolean get() = items.isEmpty()
}