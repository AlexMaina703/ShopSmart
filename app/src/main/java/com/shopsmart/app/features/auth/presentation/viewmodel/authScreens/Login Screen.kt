package com.shopsmart.app.features.auth.presentation.viewmodel.authScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.features.auth.domain.model.AuthProvider
import com.shopsmart.app.features.auth.presentation.components.SocialSignInButtons
import com.shopsmart.app.features.auth.presentation.viewmodel.AuthViewModel
import com.shopsmart.app.features.auth.presentation.viewmodel.state.AuthState
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel(),
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val isLoading = loginState is AuthState.Loading
    val errorMessage = (loginState as? AuthState.Error)?.message

    val isEmailFormValid = email.isNotBlank() && password.isNotBlank()

    LaunchedEffect(loginState) {
        if (loginState is AuthState.Success) {
            viewModel.clearErrors()
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.LOGIN) { inclusive = true }
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Sign in to continue",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp),
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                enabled = !isLoading,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                enabled = isEmailFormValid && !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    Text("Login", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------- OR divider ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(Modifier.weight(1f))
                Text(
                    "  OR  ",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider(Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // ---------- SOCIAL ----------
            SocialSignInButtons(
                enabled = !isLoading,
                onSignInWith = { provider, token ->
                    viewModel.socialLogin(provider, token)
                },
                onError = { message ->
                    // simplest way to surface it: reuse the viewModel's error slot
                    // (add a public `showError(msg)` method to the VM if you want)
                },
            )

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = { navController.navigate(NavRoutes.REGISTER) }) {
                Text("Don't have an account? Register")
            }
        }
    }
}