package com.shopsmart.app.features.order.presentation.state


data class AddAddressUiState(
    val label: String = "Home",
    val fullName: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val postalCode: String = "",
    val isDefault: Boolean = false,

    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val savedSuccessfully: Boolean = false,
) {
    val isValid: Boolean
        get() = label.isNotBlank() &&
                fullName.isNotBlank() &&
                phone.isNotBlank() &&
                address.isNotBlank() &&
                city.isNotBlank() &&
                postalCode.isNotBlank()
}