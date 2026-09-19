package com.shopsmart.app.features.home.presentation.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.core.ui.theme.ErrorMessage
import com.shopsmart.app.core.ui.theme.LoadingIndicator
import com.shopsmart.app.features.home.presentation.components.ProductDetailBottomBar
import com.shopsmart.app.features.home.presentation.components.ProductDetailTopBar
import com.shopsmart.app.features.home.presentation.components.ProductImagePager
import com.shopsmart.app.features.home.presentation.components.QuantitySelector
import com.shopsmart.app.features.home.presentation.components.SpecsRow
import com.shopsmart.app.features.home.presentation.viewmodels.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController,
    viewModel: ProductDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(productId) {
        viewModel.load(productId)
    }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeToast()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ProductDetailTopBar(
                isFavorite = state.isFavorite,
                cartItemCount = state.cartItemCount,
                onBack = { navController.popBackStack() },
                onFavoriteClick = viewModel::toggleFavorite,
                onCartClick = { /* navigate to cart */ },
            )
        },
        bottomBar = {
            if (state.product != null) {
                ProductDetailBottomBar(
                    isAddingToCart = state.isAddingToCart,
                    onAddToCart = viewModel::addToCart,
                    onBuyNow = viewModel::buyNow,
                )
            }
        },
    ) { padding ->
        when {
            state.isLoading && state.product == null -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }
            state.errorMessage != null && state.product == null -> {
                ErrorMessage(
                    message = state.errorMessage ?: "Failed to load product",
                    modifier = Modifier.padding(padding),
                )
            }
            state.product != null -> {
                val product = state.product!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    // ---------- IMAGE CAROUSEL ----------
                    ProductImagePager(
                        images = product.images,
                        selectedIndex = state.selectedImageIndex,
                        onIndexChange = viewModel::selectImage,
                    )

                    Spacer(Modifier.height(20.dp))

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                        // ---------- NAME ----------
                        Text(
                            text = product.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Spacer(Modifier.height(8.dp))

                        // ---------- PRICE ----------
                        Text(
                            text = "KSh ${formatPrice(product.price)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Spacer(Modifier.height(8.dp))

                        // ---------- RATING + STOCK ----------
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "${product.rating} (${product.reviewCount} reviews)",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = if (product.isInStock) "In Stock" else "Out of Stock",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (product.isInStock)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error,
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        // ---------- SPECS ----------
                        SpecsRow(specs = product.specs)

                        Spacer(Modifier.height(24.dp))

                        // ---------- DESCRIPTION ----------
                        Text(
                            "Description",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = product.description ?: "No description available.",
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Spacer(Modifier.height(24.dp))

                        // ---------- QUANTITY ----------
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "Quantity",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f),
                            )
                            QuantitySelector(
                                quantity = state.quantity,
                                onDecrement = viewModel::decrementQuantity,
                                onIncrement = viewModel::incrementQuantity,
                                max = product.stock.coerceAtLeast(1),
                            )
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

private fun formatPrice(value: Double): String {
    return if (value % 1.0 == 0.0) value.toInt().toString()
    else String.format(Locale.US, "%,.2f", value)
}