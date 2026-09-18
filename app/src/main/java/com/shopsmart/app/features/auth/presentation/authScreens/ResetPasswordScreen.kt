package com.shopsmart.app.features.auth.presentation.authScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun ResetPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = koinViewModel()
) {
    var token by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val state by authViewModel.resetState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val isLoading = state is AuthState.Loading
    val errorMessage = (state as? AuthState.Error)?.message

    val passwordsMatch = newPassword == confirmPassword
    val isFormValid = token.isNotBlank() &&
            newPassword.length >= 6 &&
            passwordsMatch

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            snackbarHostState.showSnackbar("Password reset. Please login.")
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
            authViewModel.clearResetState()
        }
    }

    DisposableEffect(Unit) {
        onDispose { authViewModel.clearResetState() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Reset Password") },
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
            Text("Create New Password", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Enter the token from your email and your new password.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                label = { Text("Reset Token") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password"
                            else "Show password"
                        )
                    }
                }
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                isError = confirmPassword.isNotEmpty() && !passwordsMatch,
                supportingText = if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                    { Text("Passwords do not match") }
                } else null,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation()
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
                onClick = { authViewModel.resetPassword(token.trim(), newPassword) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = isFormValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Reset Password", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}