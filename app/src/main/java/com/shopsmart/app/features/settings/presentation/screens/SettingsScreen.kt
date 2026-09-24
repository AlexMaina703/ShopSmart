package com.shopsmart.app.features.settings.presentation.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.features.settings.presentation.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Dark mode
            SettingsRow(
                icon = { Icon(Icons.Default.DarkMode, contentDescription = null) },
                title = "Dark Mode",
                subtitle = if (state.darkMode) "On" else "Off",
                trailing = {
                    Switch(
                        checked = state.darkMode,
                        onCheckedChange = viewModel::setDarkMode,
                    )
                },
            )

            // Notifications
            SettingsRow(
                icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                title = "Push Notifications",
                subtitle = if (state.notificationsEnabled) "Enabled" else "Disabled",
                trailing = {
                    Switch(
                        checked = state.notificationsEnabled,
                        onCheckedChange = viewModel::setNotificationsEnabled,
                    )
                },
            )

            // Language
            SettingsRow(
                icon = { Icon(Icons.Default.Language, contentDescription = null) },
                title = "Language",
                subtitle = languageName(state.language),
                onClick = { showLanguageDialog = true },
            )

            HorizontalDivider(Modifier.padding(horizontal = 16.dp))

            // About
            SettingsRow(
                icon = { Icon(Icons.Default.Info, contentDescription = null) },
                title = "About ShopSmart",
                subtitle = "Version 1.0.0",
                onClick = { /* TODO: open about */ },
            )
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Choose Language") },
            text = {
                Column {
                    listOf("en" to "English", "sw" to "Kiswahili", "fr" to "Français").forEach { (code, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLanguage(code)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = state.language == code,
                                onClick = {
                                    viewModel.setLanguage(code)
                                    showLanguageDialog = false
                                },
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(name, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) { Text("Close") }
            },
        )
    }
}

@Composable
private fun SettingsRow(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(
                subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        trailing?.invoke()
    }
}

private fun languageName(code: String): String = when (code) {
    "en" -> "English"
    "sw" -> "Kiswahili"
    "fr" -> "Français"
    else -> code
}