package com.shopsmart.app.features.cart.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.core.ui.theme.EmptyState
import com.shopsmart.app.core.ui.theme.ErrorMessage
import com.shopsmart.app.core.ui.theme.LoadingIndicator
import com.shopsmart.app.features.cart.presentation.components.CartItemRow
import com.shopsmart.app.features.cart.presentation.components.formatPrice
import com.shopsmart.app.features.cart.presentation.viewmodels.CartViewModel
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showClearDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeToast()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Cart", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!state.cart.isEmpty) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear cart")
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (!state.cart.isEmpty) {
                CartBottomBar(
                    subtotal = state.cart.subtotal,
                    shipping = state.cart.shipping,
                    total = state.cart.total,
                    itemCount = state.cart.itemCount,
                    onCheckout = { navController.navigate(NavRoutes.CHECKOUT) },
                )
            }
        },
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))

            state.errorMessage != null -> ErrorMessage(
                message = state.errorMessage ?: "Something went wrong",
                modifier = Modifier.padding(padding),
            )

            state.cart.isEmpty -> EmptyState(
                message = "Your cart is empty",
                modifier = Modifier.padding(padding),
            )

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(state.cart.items, key = { it.id }) { item ->
                        CartItemRow(
                            item = item,
                            isUpdating = state.updatingItemId == item.id,
                            onIncrement = { viewModel.increment(item.id, item.quantity) },
                            onDecrement = { viewModel.decrement(item.id, item.quantity) },
                            onRemove = { viewModel.removeItem(item.id) },
                            onClick = {
                                navController.navigate(NavRoutes.productDetail(item.product.id))
                            },
                        )
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear cart?") },
            text = { Text("This removes all items from your cart.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearCart()
                    showClearDialog = false
                }) { Text("Clear", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun CartBottomBar(
    subtotal: Double,
    shipping: Double,
    total: Double,
    itemCount: Int,
    onCheckout: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SummaryRow("Subtotal ($itemCount items)", subtotal)
            SummaryRow("Shipping", shipping)
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            SummaryRow("Total", total, bold = true)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text("Proceed to Checkout", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: Double, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            label,
            fontSize = if (bold) 15.sp else 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = if (bold) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            "KSh ${formatPrice(value)}",
            fontSize = if (bold) 16.sp else 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
            color = if (bold) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
        )
    }
}