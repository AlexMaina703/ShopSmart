package com.shopsmart.app.features.order.presentation.state

import com.shopsmart.app.features.order.domain.model.TrackingEvent

data class TrackOrderUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val events: List<TrackingEvent> = emptyList(),
)