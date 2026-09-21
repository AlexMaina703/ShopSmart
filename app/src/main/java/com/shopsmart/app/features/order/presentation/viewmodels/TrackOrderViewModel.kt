package com.shopsmart.app.features.order.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.TrackingEvent
import com.shopsmart.app.features.order.domain.usecase.GetOrderTrackingUseCase
import com.shopsmart.app.features.order.presentation.state.TrackOrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrackOrderViewModel(
    private val getOrderTrackingUseCase: GetOrderTrackingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackOrderUiState())
    val uiState: StateFlow<TrackOrderUiState> = _uiState.asStateFlow()

    fun load(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val r = getOrderTrackingUseCase(orderId)) {
                is AppResult.Success -> _uiState.update {
                    // Server returns newest first; we want oldest → newest for the timeline
                    it.copy(isLoading = false, events = r.data.reversed())
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = r.exception.message ?: "Failed to load tracking",
                    )
                }
            }
        }
    }

    fun loadFromInline(events: List<TrackingEvent>) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                // Server returns newest-first; timeline wants oldest-first
                events = events.sortedBy { e -> e.timestamp },
            )
        }
    }
}