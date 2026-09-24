package com.shopsmart.app.features.order.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.usecase.DeleteAddressUseCase
import com.shopsmart.app.features.order.domain.usecase.GetAddressesUseCase
import com.shopsmart.app.features.order.presentation.state.AddressesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddressesViewModel(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressesUiState())
    val uiState: StateFlow<AddressesUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val r = getAddressesUseCase()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, addresses = r.data)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = r.exception.message ?: "Failed to load addresses",
                    )
                }
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(deletingId = id) }
            when (val r = deleteAddressUseCase(id)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            deletingId = null,
                            addresses = it.addresses.filterNot { a -> a.id == id },
                            toastMessage = "Address removed",
                        )
                    }
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        deletingId = null,
                        toastMessage = r.exception.message ?: "Failed to remove",
                    )
                }
            }
        }
    }

    fun consumeToast() = _uiState.update { it.copy(toastMessage = null) }
}