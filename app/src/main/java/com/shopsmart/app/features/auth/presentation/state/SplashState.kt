package com.shopsmart.app.features.auth.presentation.state

sealed class SplashState {
    object Loading : SplashState()
    object NeedsOnboarding : SplashState()
    object Authenticated : SplashState()
    object Unauthenticated : SplashState()
}