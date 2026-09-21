package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.PaymentMethod
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class AddPaymentMethodUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(
        cardNumber: String,
        cardType: String,
        expiryMonth: Int,
        expiryYear: Int,
        cvv: String,
        isDefault: Boolean = false,
    ): AppResult<PaymentMethod> = repo.addPaymentMethod(
        cardNumber, cardType, expiryMonth, expiryYear, cvv, isDefault
    )
}

class DeletePaymentMethodUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(id: String): AppResult<Unit> = repo.deletePaymentMethod(id)
}