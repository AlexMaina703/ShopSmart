package com.shopsmart.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shopsmart.app.features.auth.presentation.authScreens.ForgotPasswordScreen
import com.shopsmart.app.features.auth.presentation.authScreens.LoginScreen
import com.shopsmart.app.features.auth.presentation.authScreens.OnboardingScreen
import com.shopsmart.app.features.auth.presentation.authScreens.RegisterScreen
import com.shopsmart.app.features.auth.presentation.authScreens.ResetPasswordScreen
import com.shopsmart.app.features.auth.presentation.authScreens.SplashScreen
import com.shopsmart.app.features.home.presentation.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavRoutes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(navController)
        }
        composable(NavRoutes.ONBOARDING) {
            OnboardingScreen(navController)
        }
        composable(NavRoutes.LOGIN) {
            LoginScreen(navController)
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController)
        }
        composable(NavRoutes.HOME) {
            HomeScreen()
        }

        composable(NavRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(navController)
        }
        composable(NavRoutes.RESET_PASSWORD) {
            ResetPasswordScreen(navController)
        }

    }
}