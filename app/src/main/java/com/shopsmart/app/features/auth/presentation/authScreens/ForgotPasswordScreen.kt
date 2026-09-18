package com.shopsmart.app.features.auth.presentation.authScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.features.auth.presentation.state.AuthState
import com.shopsmart.app.features.auth.presentation.viewmodel.AuthViewModel
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = koinViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    val state by authViewModel.forgotState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val isLoading = state is AuthState.Loading
    val errorMessage = (state as? AuthState.Error)?.message

    LaunchedEffect(state) {
        when (state) {
            is AuthState.Success -> {
                snackbarHostState.showSnackbar("Reset link sent. Check your email.")
                navController.navigate(NavRoutes.RESET_PASSWORD)
                authViewModel.clearForgotState()
            }
            else -> Unit
        }
    }

    DisposableEffect(Unit) {
        onDispose { authViewModel.clearForgotState() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        enabled = !isLoading
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Text("Forgot Password?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "No worries! Enter your email and we'll send you reset instructions.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                enabled = !isLoading
            )

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { authViewModel.forgotPassword(email.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = email.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Send Reset Link", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}