package com.shopsmart.app.features.order.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import com.shopsmart.app.core.ui.theme.ErrorMessage
import com.shopsmart.app.core.ui.theme.LoadingIndicator
import com.shopsmart.app.features.order.domain.model.Address
import com.shopsmart.app.features.order.domain.model.PaymentMethod
import com.shopsmart.app.features.order.presentation.viewmodels.CheckoutViewModel
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    viewModel: CheckoutViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    val addressAdded = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("address_added", false)
        ?.collectAsStateWithLifecycle()

    val paymentAdded = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("payment_added", false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(addressAdded?.value, paymentAdded?.value) {
        if (addressAdded?.value == true || paymentAdded?.value == true) {
            viewModel.load()
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("address_added")
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("payment_added")
        }
    }


    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeToast()
        }
    }

    LaunchedEffect(state.placedOrderId) {
        state.placedOrderId?.let { orderId ->
            viewModel.consumeOrderId()
            navController.navigate(NavRoutes.orderSuccess(orderId)) {
                popUpTo(NavRoutes.CART) { inclusive = true }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = viewModel::placeOrder,
                        enabled = state.canPlaceOrder,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        if (state.isPlacingOrder) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        } else {
                            Text("Place Order", fontWeight = FontWeight.SemiBold)
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

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SectionHeader(
                    "Shipping Address",
                    showAdd = true,
                    onAdd = { navController.navigate(NavRoutes.ADD_ADDRESS) },
                )
                if (state.addresses.isEmpty()) {
                    EmptyHint("No addresses yet. Add one to continue.")
                } else {
                    state.addresses.forEach { address ->
                        AddressCard(
                            address = address,
                            selected = address.id == state.selectedAddressId,
                            onSelect = { viewModel.selectAddress(address.id) },
                        )
                    }
                }

                SectionHeader(
                    "Payment Method",
                    showAdd = true,
                    onAdd = { navController.navigate(NavRoutes.ADD_PAYMENT_METHOD) },
                )
                if (state.paymentMethods.isEmpty()) {
                    EmptyHint("No payment methods yet. Add one to continue.")
                } else {
                    state.paymentMethods.forEach { pm ->
                        PaymentCard(
                            payment = pm,
                            selected = pm.id == state.selectedPaymentId,
                            onSelect = { viewModel.selectPayment(pm.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, showAdd: Boolean = false, onAdd: () -> Unit = {}) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        if (showAdd) {
            TextButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add", fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun EmptyHint(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp),
    ) {
        Text(text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun AddressCard(
    address: Address,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)

    Card(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .border(if (selected) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(address.label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    if (address.isDefault) {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Default",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp),
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(address.fullName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text(address.summary, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(address.phone, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (selected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

@Composable
private fun PaymentCard(
    payment: PaymentMethod,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)

    Card(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .border(if (selected) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(payment.display, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Expires ${payment.expiry}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (selected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}