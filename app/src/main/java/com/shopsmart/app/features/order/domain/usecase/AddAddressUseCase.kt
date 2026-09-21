package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.Address
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class AddAddressUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(
        label: String,
        fullName: String,
        phone: String,
        address: String,
        city: String,
        postalCode: String,
        isDefault: Boolean = false,
    ): AppResult<Address> = repo.addAddress(
        label, fullName, phone, address, city, postalCode, isDefault
    )
}

class DeleteAddressUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(id: String): AppResult<Unit> = repo.deleteAddress(id)
}