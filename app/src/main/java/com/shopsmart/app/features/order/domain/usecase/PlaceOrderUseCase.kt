package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.Order
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class PlaceOrderUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(
        addressId: String,
        paymentMethodId: String,
        shippingMethod: String = "standard",
    ): AppResult<Order> = repo.placeOrder(addressId, paymentMethodId, shippingMethod)
}