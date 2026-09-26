package com.shopsmart.app.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.datastore.DataStoreManager
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.usecase.GetBannersUseCase
import com.shopsmart.app.features.home.domain.usecase.GetCategoriesUseCase
import com.shopsmart.app.features.home.domain.usecase.GetFeaturedProductsUseCase
import com.shopsmart.app.features.home.presentation.state.HomeUiState
import com.shopsmart.app.features.notification.domain.usecase.GetUnreadCountUseCase
import com.shopsmart.app.features.wishlist.domain.usecase.AddToWishlistUseCase
import com.shopsmart.app.features.wishlist.domain.usecase.GetWishlistUseCase
import com.shopsmart.app.features.wishlist.domain.usecase.RemoveFromWishlistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase,
    private val getBannersUseCase: GetBannersUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val getUnreadCountUseCase: GetUnreadCountUseCase,
    private val dataStore: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _wishlistMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val wishlistMap: StateFlow<Map<String, String>> = _wishlistMap.asStateFlow()

    //  drawer header
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    init {
        loadHome()
        loadWishlist()
        loadUnreadCount()
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            dataStore.userName.collect { _userName.value = it }
        }
        viewModelScope.launch {
            dataStore.userEmail.collect { _userEmail.value = it }
        }
    }

    fun refreshUnreadCount() = loadUnreadCount()

    private fun loadUnreadCount() {
        viewModelScope.launch {
            when (val r = getUnreadCountUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(unreadNotificationCount = r.data)
                }
                is AppResult.Failure -> Unit
            }
        }
    }

    private fun loadWishlist() {
        viewModelScope.launch {
            when (val r = getWishlistUseCase()) {
                is AppResult.Success -> _wishlistMap.value =
                    r.data.associate { it.product.id to it.id }
                else -> Unit
            }
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            val existingItemId = _wishlistMap.value[productId]
            if (existingItemId != null) {
                when (removeFromWishlistUseCase(existingItemId)) {
                    is AppResult.Success -> {
                        _wishlistMap.update { it - productId }
                        _uiState.update { it.copy(toastMessage = "Removed from wishlist") }
                    }
                    is AppResult.Failure -> Unit
                }
            } else {
                when (addToWishlistUseCase(productId)) {
                    is AppResult.Success -> {
                        loadWishlist()
                        _uiState.update { it.copy(toastMessage = "Added to wishlist") }
                    }
                    is AppResult.Failure -> Unit
                }
            }
        }
    }

    fun consumeToast() = _uiState.update { it.copy(toastMessage = null) }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val banners = getBannersUseCase()
            val categories = getCategoriesUseCase()
            val featured = getFeaturedProductsUseCase()

            val catList = (categories as? AppResult.Success)?.data.orEmpty()
            val featList = (featured as? AppResult.Success)?.data.orEmpty()
            val error = when {
                categories is AppResult.Failure -> categories.exception.message
                featured is AppResult.Failure -> featured.exception.message
                else -> null
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = error,
                    banners = banners,
                    categories = catList,
                    featuredProducts = featList,
                )
            }
        }
    }
}