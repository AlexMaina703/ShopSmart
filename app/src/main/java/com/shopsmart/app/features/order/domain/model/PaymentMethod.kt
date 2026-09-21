package com.shopsmart.app.features.order.domain.model

data class PaymentMethod(
    val id: String,
    val cardType: String,
    val lastFour: String,
    val provider: String?,
    val expiryMonth: Int,
    val expiryYear: Int,
    val isDefault: Boolean,
) {
    val display: String get() = "$cardType •••• $lastFour"
    val expiry: String get() = "%02d/%d".format(expiryMonth, expiryYear)
}