package com.shopsmart.app.features.order.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.core.ui.theme.EmptyState
import com.shopsmart.app.core.ui.theme.ErrorMessage
import com.shopsmart.app.core.ui.theme.LoadingIndicator
import com.shopsmart.app.features.order.domain.model.TrackingEvent
import com.shopsmart.app.features.order.presentation.viewmodels.TrackOrderViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderScreen(
    orderId: String,
    navController: NavController,
    viewModel: TrackOrderViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // 👇 Prefer inline tracking from the previous screen if available
    val trackingJson = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("tracking_json")

    LaunchedEffect(orderId) {
        val inline = trackingJson?.let { json ->
            try {
                val type = object : com.google.gson.reflect.TypeToken<List<com.shopsmart.app.features.order.domain.model.TrackingEvent>>() {}.type
                com.google.gson.Gson().fromJson<List<com.shopsmart.app.features.order.domain.model.TrackingEvent>>(json, type)
            } catch (_: Exception) { null }
        }
        if (!inline.isNullOrEmpty()) {
            viewModel.loadFromInline(inline)
        } else {
            viewModel.load(orderId)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Order", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            state.errorMessage != null -> ErrorMessage(
                message = state.errorMessage ?: "Something went wrong",
                modifier = Modifier.padding(padding),
            )
            state.events.isEmpty() -> EmptyState(
                message = "No tracking information yet",
                modifier = Modifier.padding(padding),
            )
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(20.dp),
                ) {
                    itemsIndexed(state.events) { index, event ->
                        TimelineStep(
                            event = event,
                            isLatest = index == state.events.lastIndex,
                            isFirst = index == 0,
                            isLast = index == state.events.lastIndex,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineStep(
    event: TrackingEvent,
    isLatest: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
) {
    val dotColor = if (isLatest) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)

    Row(modifier = Modifier.fillMaxWidth()) {
        // Timeline column (dot + connector)
        Column(
            modifier = Modifier.width(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top connector
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(12.dp)
                    .background(
                        if (isFirst) Color.Transparent
                        else dotColor.copy(alpha = 0.3f)
                    ),
            )
            // Dot
            Box(
                modifier = Modifier
                    .size(if (isLatest) 16.dp else 12.dp)
                    .clip(CircleShape)
                    .background(dotColor),
            )
            // Bottom connector
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(if (isLast) 12.dp else 60.dp)
                    .background(
                        if (isLast) Color.Transparent
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
            )
        }

        Spacer(Modifier.width(12.dp))

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        ) {
            Text(
                event.status.replaceFirstChar { it.uppercase() },
                fontWeight = if (isLatest) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isLatest) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
            )
            event.note?.let {
                Spacer(Modifier.height(2.dp))
                Text(it, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            event.location?.let {
                Spacer(Modifier.height(2.dp))
                Text(it, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                formatTimestamp(event.timestamp),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            )
        }
    }
}

/**
 * Minimal ISO-8601 formatter: "2026-08-28T14:00:00" → "28 Aug 2026, 14:00"
 */
private fun formatTimestamp(raw: String): String {
    return try {
        val parts = raw.split("T")
        if (parts.size != 2) return raw
        val date = parts[0]                 // 2026-08-28
        val time = parts[1].take(5)         // 14:00
        val (y, m, d) = date.split("-")
        val monthName = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ).getOrElse(m.toInt() - 1) { m }
        "$d $monthName $y, $time"
    } catch (e: Exception) {
        raw
    }
}