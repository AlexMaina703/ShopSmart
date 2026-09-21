package com.shopsmart.app.features.order.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.shopsmart.app.core.ui.theme.ErrorMessage
import com.shopsmart.app.core.ui.theme.LoadingIndicator
import com.shopsmart.app.features.order.domain.model.Order
import com.shopsmart.app.features.order.presentation.viewmodels.OrderDetailViewModel
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavController,
    viewModel: OrderDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) { viewModel.load(orderId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        bottomBar = {
            state.order?.let { order ->
                Surface(shadowElevation = 8.dp) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("tracking_json", com.google.gson.Gson().toJson(order.tracking))
                                navController.navigate(NavRoutes.trackOrder(order.id))
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Text("Track Order", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        },
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            state.errorMessage != null -> ErrorMessage(
                message = state.errorMessage ?: "Something went wrong",
                modifier = Modifier.padding(padding),
            )
            state.order != null -> OrderDetailContent(
                order = state.order!!,
                modifier = Modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun OrderDetailContent(order: Order, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // ---------- HEADER ----------
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            order.orderNumber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            order.status.replaceFirstChar { it.uppercase() },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                    RoundedCornerShape(6.dp),
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                        )
                    }
                    order.createdAt?.let {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        // ---------- ITEMS ----------
        item {
            Text("Items (${order.items.size})", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
        items(order.items, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp),
            ) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        item.productImage?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = item.productName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.productName, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        val variants = listOfNotNull(item.color, item.storage)
                        if (variants.isNotEmpty()) {
                            Text(
                                variants.joinToString(" • "),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text("Qty: ${item.quantity}", fontSize = 12.sp)
                    }
                    Text(
                        "KSh ${formatPrice(item.price * item.quantity)}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        // ---------- SHIPPING ADDRESS ----------
        order.shippingAddress?.let { addr ->
            item {
                SectionCard(title = "Shipping Address") {
                    Text(addr.fullName, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    Text(
                        addr.summary,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        addr.phone,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // ---------- PAYMENT METHOD ----------
        order.paymentMethod?.let { pm ->
            item {
                SectionCard(title = "Payment Method") {
                    Text(pm.display, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    Text(
                        "Expires ${pm.expiry}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // ---------- LATEST UPDATE (inline tracking preview) ----------
        if (order.tracking.isNotEmpty()) {
            item {
                SectionCard(title = "Latest Update") {
                    val latest = order.tracking.maxByOrNull { it.timestamp }
                    if (latest != null) {
                        Text(
                            latest.status,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        latest.note?.let {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                it,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            formatTimestamp(latest.timestamp),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        // ---------- ORDER SUMMARY ----------
        item {
            SectionCard(title = "Order Summary") {
                SummaryRow("Subtotal", order.subtotal)
                SummaryRow("Shipping", order.shipping)
                HorizontalDivider(Modifier.padding(vertical = 6.dp))
                SummaryRow("Total", order.total, bold = true)
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: Double, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            label,
            fontSize = if (bold) 15.sp else 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        )
        Text(
            "KSh ${formatPrice(value)}",
            fontSize = if (bold) 15.sp else 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
            color = if (bold) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
        )
    }
}

private fun formatPrice(v: Double): String =
    if (v % 1.0 == 0.0) v.toInt().toString()
    else String.format(java.util.Locale.US, "%,.2f", v)

/**
 * Minimal ISO-8601 formatter: "2026-08-28T14:00:00" → "28 Aug 2026, 14:00"
 */
private fun formatTimestamp(raw: String): String {
    return try {
        val parts = raw.split("T")
        if (parts.size != 2) return raw
        val date = parts[0]
        val time = parts[1].take(5)
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