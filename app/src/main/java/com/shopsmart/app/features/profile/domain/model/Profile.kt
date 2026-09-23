package com.shopsmart.app.features.profile.domain.model

import com.shopsmart.app.features.order.domain.model.Address
import com.shopsmart.app.features.order.domain.model.PaymentMethod

data class Profile(
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String?,
    val avatarUrl: String?,
    val provider: String?,
    val emailVerified: Boolean,
    val createdAt: String?,
    val addresses: List<Address>,
    val paymentMethods: List<PaymentMethod>,
) {
    val initials: String
        get() = fullName
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
            .ifBlank { "?" }
}