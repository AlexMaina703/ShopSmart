package com.shopsmart.app.features.order.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.usecase.AddAddressUseCase
import com.shopsmart.app.features.order.presentation.state.AddAddressUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddAddressViewModel(
    private val addAddressUseCase: AddAddressUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState: StateFlow<AddAddressUiState> = _uiState.asStateFlow()

    fun setLabel(v: String) = _uiState.update { it.copy(label = v) }
    fun setFullName(v: String) = _uiState.update { it.copy(fullName = v) }
    fun setPhone(v: String) = _uiState.update { it.copy(phone = v) }
    fun setAddress(v: String) = _uiState.update { it.copy(address = v) }
    fun setCity(v: String) = _uiState.update { it.copy(city = v) }
    fun setPostalCode(v: String) = _uiState.update { it.copy(postalCode = v) }
    fun setDefault(v: Boolean) = _uiState.update { it.copy(isDefault = v) }

    fun save() {
        val s = _uiState.value
        if (!s.isValid || s.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            when (val r = addAddressUseCase(
                label = s.label.trim(),
                fullName = s.fullName.trim(),
                phone = s.phone.trim(),
                address = s.address.trim(),
                city = s.city.trim(),
                postalCode = s.postalCode.trim(),
                isDefault = s.isDefault,
            )) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isSaving = false, savedSuccessfully = true)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = r.exception.message ?: "Failed to save address",
                    )
                }
            }
        }
    }
}