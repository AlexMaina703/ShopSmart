package com.shopsmart.app.features.order.domain.model

data class Address(
    val id: String,
    val label: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val isDefault: Boolean,
) {
    val summary: String get() = "$address, $city $postalCode"
}