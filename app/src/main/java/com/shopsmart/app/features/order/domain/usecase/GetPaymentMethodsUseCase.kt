package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.PaymentMethod
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class GetPaymentMethodsUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(): AppResult<List<PaymentMethod>> = repo.getPaymentMethods()
}