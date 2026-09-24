package com.shopsmart.app.features.settings.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.datastore.DataStoreManager
import com.shopsmart.app.features.settings.presentation.state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.darkMode.collect { v ->
                _uiState.update { it.copy(darkMode = v) }
            }
        }
        viewModelScope.launch {
            dataStore.notificationsEnabled.collect { v ->
                _uiState.update { it.copy(notificationsEnabled = v) }
            }
        }
        viewModelScope.launch {
            dataStore.language.collect { v ->
                _uiState.update { it.copy(language = v) }
            }
        }
    }

    fun setDarkMode(v: Boolean) {
        viewModelScope.launch { dataStore.setDarkMode(v) }
    }

    fun setNotificationsEnabled(v: Boolean) {
        viewModelScope.launch { dataStore.setNotificationsEnabled(v) }
    }

    fun setLanguage(v: String) {
        viewModelScope.launch { dataStore.setLanguage(v) }
    }
}