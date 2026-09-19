package com.shopsmart.app.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.usecase.GetBannersUseCase
import com.shopsmart.app.features.home.domain.usecase.GetCategoriesUseCase
import com.shopsmart.app.features.home.domain.usecase.GetFeaturedProductsUseCase
import com.shopsmart.app.features.home.presentation.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase,
    private val getBannersUseCase: GetBannersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Banners are local → instant
            val banners = getBannersUseCase()

            // Fire both API calls in parallel
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