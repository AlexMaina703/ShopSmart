package com.shopsmart.app.features.home.presentation.state


import com.shopsmart.app.features.home.domain.model.Banner
import com.shopsmart.app.features.home.domain.model.Category
import com.shopsmart.app.features.home.domain.model.Product

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val banners: List<Banner> = emptyList(),
    val categories: List<Category> = emptyList(),
    val featuredProducts: List<Product> = emptyList(),
    val cartItemCount: Int = 0,
    val unreadNotificationCount: Int = 0,
    val toastMessage: String? = null,
)