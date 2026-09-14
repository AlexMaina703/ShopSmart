package com.shopsmart.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shopsmart.app.features.auth.presentation.screens.RegisterScreen
import com.shopsmart.app.features.auth.presentation.viewmodel.authScreens.LoginScreen
import com.shopsmart.app.features.home.presentation.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavRoutes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(navController)
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController)
        }
        composable(NavRoutes.HOME) {
            HomeScreen()
        }
    }
}