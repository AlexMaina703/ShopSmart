package com.shopsmart.app.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.domain.model.AuthProvider
import com.shopsmart.app.features.auth.domain.usecase.ForgotPasswordUseCase
import com.shopsmart.app.features.auth.domain.usecase.LoginUseCase
import com.shopsmart.app.features.auth.domain.usecase.OnboardingUseCase
import com.shopsmart.app.features.auth.domain.usecase.RegisterUseCase
import com.shopsmart.app.features.auth.domain.usecase.ResetPasswordUseCase
import com.shopsmart.app.features.auth.domain.usecase.SocialLoginUseCase
import com.shopsmart.app.features.auth.domain.usecase.ValidateTokenUseCase
import com.shopsmart.app.features.auth.presentation.state.AuthState
import com.shopsmart.app.features.auth.presentation.state.SplashState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val socialLoginUseCase: SocialLoginUseCase,
    private val onboardingUseCase: OnboardingUseCase,
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,   // NEW
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    private val _forgotState = MutableStateFlow<AuthState>(AuthState.Idle)
    val forgotState: StateFlow<AuthState> = _forgotState.asStateFlow()

    private val _resetState = MutableStateFlow<AuthState>(AuthState.Idle)
    val resetState: StateFlow<AuthState> = _resetState.asStateFlow()

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    fun runSplashFlow() {
        viewModelScope.launch {
            _splashState.value = SplashState.Loading

            // 1. Has the user finished onboarding?
            val onboarded =
                (onboardingUseCase.isCompleted() as? AppResult.Success)?.data ?: false

            if (!onboarded) {
                _splashState.value = SplashState.NeedsOnboarding
                return@launch
            }

            // 2. Validate stored token with backend
            when (validateTokenUseCase()) {
                is AppResult.Success -> _splashState.value = SplashState.Authenticated
                is AppResult.Failure -> _splashState.value = SplashState.Unauthenticated
            }
        }
    }

    /** Called when the user finishes or skips onboarding. */
    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingUseCase.complete()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            val result = loginUseCase(email, password)
            _loginState.value = when (result) {
                is AppResult.Success -> AuthState.Success(result.data.user)
                is AppResult.Failure -> AuthState.Error(
                    result.exception.message ?: "Login failed"
                )
            }
        }
    }

    fun register(fullName: String, email: String, password: String, phone: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            val result = registerUseCase(fullName, email, password, phone)
            _registerState.value = when (result) {
                is AppResult.Success -> AuthState.Success(result.data.user)
                is AppResult.Failure -> AuthState.Error(
                    result.exception.message ?: "Registration failed"
                )
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

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _forgotState.value = AuthState.Loading
            _forgotState.value = when (val result = forgotPasswordUseCase(email)) {
                is AppResult.Success -> AuthState.Success(null)
                is AppResult.Failure -> AuthState.Error(
                    result.exception.message ?: "Failed to send reset link"
                )
            }
        }
    }

    fun resetPassword(token: String, newPassword: String) {
        viewModelScope.launch {
            _resetState.value = AuthState.Loading
            _resetState.value = when (val result = resetPasswordUseCase(token, newPassword)) {
                is AppResult.Success -> AuthState.Success(null)
                is AppResult.Failure -> AuthState.Error(
                    result.exception.message ?: "Failed to reset password"
                )
            }
        }
    }

    fun clearForgotState() { _forgotState.value = AuthState.Idle }
    fun clearResetState()  { _resetState.value = AuthState.Idle }

    fun clearErrors() {
        if (_loginState.value is AuthState.Error) _loginState.value = AuthState.Idle
        if (_registerState.value is AuthState.Error) _registerState.value = AuthState.Idle
    }

    fun showError(message: String) {
        _loginState.value = AuthState.Error(message)
    }
}