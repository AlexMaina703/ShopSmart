package com.shopsmart.app.features.home.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shopsmart.app.core.ui.theme.ErrorMessage
import com.shopsmart.app.core.ui.theme.LoadingIndicator
import com.shopsmart.app.features.home.presentation.components.*
import com.shopsmart.app.features.home.presentation.viewmodels.HomeViewModel
import com.shopsmart.app.navigation.NavRoutes
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val wishlistMap by viewModel.wishlistMap.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var currentBottomRoute by remember { mutableStateOf("home") }

    // Show toast messages coming from ViewModel (favourite toggles, etc.)
    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeToast()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            HomeTopBar(
                cartItemCount = state.cartItemCount,
                onMenuClick = { /* open drawer later */ },
                onSearchClick = { /* TODO: navigate to search */ },
                onCartClick = { navController.navigate(NavRoutes.CART) },
            )
        },
        bottomBar = {
            ShopSmartBottomBar(
                items = defaultBottomNavItems(state.cartItemCount),
                currentRoute = currentBottomRoute,
                onItemClick = { item ->
                    currentBottomRoute = item.route
                    when (item.route) {
                        "home" -> Unit
                        "categories" -> navController.navigate(NavRoutes.CATEGORIES)
                        "cart" -> navController.navigate(NavRoutes.CART)
                        "orders" -> navController.navigate(NavRoutes.ORDERS)
                        "profile" -> navController.navigate(NavRoutes.PROFILE)
                    }
                },
            )
        },
    ) { padding ->

        when {
            state.isLoading && state.featuredProducts.isEmpty() -> {
                LoadingIndicator(modifier = Modifier.padding(padding))
            }

            state.errorMessage != null && state.featuredProducts.isEmpty() -> {
                ErrorMessage(
                    message = state.errorMessage ?: "Something went wrong",
                    modifier = Modifier.padding(padding),
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(Modifier.height(8.dp))

                    // ---------- BANNERS ----------
                    BannerCarousel(
                        banners = state.banners,
                        onBannerClick = { banner ->
                            // Route based on the banner's targetRoute field
                            when (banner.targetRoute) {
                                "categories" -> navController.navigate(NavRoutes.CATEGORIES)
                                "cart" -> navController.navigate(NavRoutes.CART)
                                "orders" -> navController.navigate(NavRoutes.ORDERS)
                                "profile" -> navController.navigate(NavRoutes.PROFILE)
                                else -> Unit // no-op for unknown targets
                            }
                        },
                    )

                    Spacer(Modifier.height(24.dp))

                    // ---------- CATEGORIES ----------
                    CategoriesRow(
                        categories = state.categories,
                        onCategoryClick = { category ->
                            navController.navigate(
                                NavRoutes.productList(category.id, category.name)
                            )
                        },
                        onSeeAllClick = { navController.navigate(NavRoutes.CATEGORIES) },
                    )

                    Spacer(Modifier.height(24.dp))

                    // ---------- FEATURED ----------
                    FeaturedProductsGrid(
                        products = state.featuredProducts,
                        wishlistMap = wishlistMap,
                        onProductClick = { product ->
                            navController.navigate(NavRoutes.productDetail(product.id))
                        },
                        onFavoriteClick = { product ->
                            viewModel.toggleFavorite(product.id)
                        },
                        onSeeAllClick = {
                            // "See All" of featured → browse everything
                            // Uses the first category as a fallback, or a "all" route
                            navController.navigate(
                                NavRoutes.productList("", "Featured Products")
                            )
                        },
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}