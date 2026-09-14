package com.shopsmart.app.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.model.AuthProvider
import com.shopsmart.app.features.auth.domain.usecase.LoginUseCase
import com.shopsmart.app.features.auth.domain.usecase.RegisterUseCase
import com.shopsmart.app.features.auth.domain.usecase.SocialLoginUseCase
import com.shopsmart.app.features.auth.presentation.viewmodel.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val socialLoginUseCase: SocialLoginUseCase
): ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            val result = loginUseCase(email, password)
            _loginState.value = when (result) {
                is AppResult.Success -> AuthState.Success(result.data.user)
                is AppResult.Failure -> AuthState.Error(result.exception.message ?: "Login failed")
            }
        }
    }

    fun register(fullName: String, email: String, password: String, phone: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            val result = registerUseCase(fullName, email, password, phone)
            _registerState.value = when (result) {
                is AppResult.Success -> AuthState.Success(result.data.user)
                is AppResult.Failure -> AuthState.Error(result.exception.message ?: "Registration failed")
            }
        }
    }

    fun socialLogin(provider: AuthProvider, accessToken: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            val result = socialLoginUseCase(provider, accessToken)
            _loginState.value = when (result) {
                is AppResult.Success -> AuthState.Success(result.data.user)
                is AppResult.Failure -> AuthState.Error(
                    result.exception.message ?: "Sign-in failed"
                )
            }
        }
    }

    fun clearErrors() {
        if (_loginState.value is AuthState.Error) _loginState.value = AuthState.Idle
        if (_registerState.value is AuthState.Error) _registerState.value = AuthState.Idle
    }
}