package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.Order
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class GetOrdersUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(): AppResult<List<Order>> = repo.getOrders()
}