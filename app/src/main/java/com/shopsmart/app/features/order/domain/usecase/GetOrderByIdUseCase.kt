package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.Order
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class GetOrderByIdUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(id: String): AppResult<Order> = repo.getOrderById(id)
}