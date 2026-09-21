package com.shopsmart.app.features.order.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.TrackingEvent
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class GetOrderTrackingUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(id: String): AppResult<List<TrackingEvent>> =
        repo.getOrderTracking(id)
}