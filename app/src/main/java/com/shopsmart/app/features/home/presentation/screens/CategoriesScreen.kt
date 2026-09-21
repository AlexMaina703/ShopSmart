package com.shopsmart.app.features.home.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.core.ui.theme.ErrorMessage
import com.shopsmart.app.core.ui.theme.LoadingIndicator
import com.shopsmart.app.features.home.presentation.components.CategoriesTopBar
import com.shopsmart.app.features.home.presentation.components.CategorySidebar
import com.shopsmart.app.features.home.presentation.components.ProductCard
import com.shopsmart.app.features.home.presentation.components.ShopSmartBottomBar
import com.shopsmart.app.features.home.presentation.components.defaultBottomNavItems
import com.shopsmart.app.features.home.presentation.viewmodels.CategoriesViewModel
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CategoriesTopBar(
                onBack = { navController.popBackStack() },
                onSearchClick = { /* TODO: navigate to search */ },
            )
        },
        bottomBar = {
            ShopSmartBottomBar(
                items = defaultBottomNavItems(0),
                currentRoute = "categories",
                onItemClick = { item ->
                    when (item.route) {
                        "home" -> navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) { inclusive = true }
                        }
                        "categories" -> Unit
                        "cart" -> navController.navigate(NavRoutes.CART)
                        "orders" -> navController.navigate(NavRoutes.ORDERS)
                        "profile" -> { /* TODO */ }
                    }
                },
            )
        },
    ) { padding ->
        when {
            state.isLoadingCategories -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }
            state.categoriesError != null && state.categories.isEmpty() -> {
                ErrorMessage(
                    message = state.categoriesError ?: "Something went wrong",
                    modifier = Modifier.padding(padding),
                )
            }
            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(MaterialTheme.colorScheme.surface),
                ) {
                    CategorySidebar(
                        categories = state.categories,
                        selectedId = state.selectedCategoryId,
                        onSelect = viewModel::selectCategory,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    ) {
                        // Section header
                        val title = state.selectedCategory?.name ?: "Products"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                "See All",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    val cat = state.selectedCategory ?: return@clickable
                                    navController.navigate(
                                        NavRoutes.productList(cat.id, cat.name)
                                    )
                                },
                            )
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )

                        when {
                            state.isLoadingProducts -> {
                                LoadingIndicator()
                            }
                            state.productsError != null -> {
                                ErrorMessage(message = state.productsError ?: "Error")
                            }
                            state.products.isEmpty() -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        "No products in this category yet",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp,
                                    )
                                }
                            }
                            else -> {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    items(state.products, key = { it.id }) { product ->
                                        ProductCard(
                                            product = product,
                                            onClick = {
                                                navController.navigate(
                                                    NavRoutes.productDetail(product.id)
                                                )
                                            },
                                            onFavoriteClick = { /* TODO wishlist */ },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}