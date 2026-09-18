package com.shopsmart.app.features.auth.presentation.authScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shopsmart.app.features.auth.presentation.viewmodel.AuthViewModel
import com.shopsmart.app.features.auth.presentation.state.SplashState
import com.shopsmart.app.navigation.NavRoutes
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val splashState by viewModel.splashState.collectAsState()

    // Kick off the flow after the branding delay
    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.runSplashFlow()
    }

    // React to the result
    LaunchedEffect(splashState) {
        when (splashState) {
            SplashState.NeedsOnboarding -> navController.navigate(NavRoutes.ONBOARDING) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
            SplashState.Authenticated -> navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
            SplashState.Unauthenticated -> navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }
            SplashState.Loading -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🛍️", fontSize = 60.sp)
            Spacer(Modifier.height(16.dp))
            Text(
                text = "ShopSmart",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Shop smarter, live better.",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}