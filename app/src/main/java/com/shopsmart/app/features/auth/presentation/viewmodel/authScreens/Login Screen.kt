package com.shopsmart.app.features.auth.presentation.viewmodel.authScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shopsmart.app.core.ui.components.ErrorMessage
import com.shopsmart.app.core.ui.components.LoadingIndicator
import com.shopsmart.app.features.auth.presentation.viewmodel.AuthViewModel
import com.shopsmart.app.features.auth.presentation.viewmodel.state.AuthState
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel                         // ✅ new import

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()                         // ✅ use koinViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is AuthState.Success) {
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.LOGIN) { inclusive = true }
            }
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (loginState) {
                is AuthState.Loading -> LoadingIndicator()
                is AuthState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ErrorMessage((loginState as AuthState.Error).message)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.clearErrors() }) {
                            Text("Dismiss")
                        }
                    }
                }
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.login(email, password) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Login")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { navController.navigate(NavRoutes.REGISTER) }) {
                            Text("Don't have an account? Register here")
                        }
                    }
                }
            }
        }
    }
}