package com.shopsmart.app.features.profile.data.mappers

import com.shopsmart.app.features.order.data.mappers.OrderMappers.toDomain
import com.shopsmart.app.features.profile.data.remote.model.ProfileResponseDto
import com.shopsmart.app.features.profile.domain.model.Profile

object ProfileMappers {

    fun ProfileResponseDto.toDomain() = Profile(
        id = id,
        email = email,
        fullName = fullName,
        phone = phone,
        avatarUrl = avatarUrl,
        provider = provider,
        emailVerified = emailVerified,
        createdAt = createdAt,
        addresses = addresses.map { it.toDomain() },
        paymentMethods = paymentMethods.map { it.toDomain() },
    )
}