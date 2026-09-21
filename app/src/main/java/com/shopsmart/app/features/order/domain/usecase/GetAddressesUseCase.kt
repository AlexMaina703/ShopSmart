package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.Address
import com.shopsmart.app.features.order.domain.repository.OrderRepository


class GetAddressesUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(): AppResult<List<Address>> = repo.getAddresses()
}