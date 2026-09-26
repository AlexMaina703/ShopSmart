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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val wishlistMap by viewModel.wishlistMap.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val userEmail by viewModel.userEmail.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentBottomRoute by remember { mutableStateOf("home") }

    // Toast messages
    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeToast()
        }
    }

    // Refresh notification badge + cart count every time home comes back into focus
    LaunchedEffect(Unit) {
        viewModel.refreshUnreadCount()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            HomeDrawer(
                userName = userName,
                userEmail = userEmail,
                unreadCount = state.unreadNotificationCount,
                onEntryClick = { entry ->
                    scope.launch { drawerState.close() }
                    when (entry.route) {
                        "home" -> Unit
                        "categories" -> navController.navigate(NavRoutes.CATEGORIES)
                        "cart" -> navController.navigate(NavRoutes.CART)
                        "wishlist" -> navController.navigate(NavRoutes.WISHLIST)
                        "orders" -> navController.navigate(NavRoutes.ORDERS)
                        "notifications" -> navController.navigate(NavRoutes.NOTIFICATIONS)
                        "profile" -> navController.navigate(NavRoutes.PROFILE)
                        "addresses" -> navController.navigate(NavRoutes.ADDRESSES)
                        "payment_methods" -> navController.navigate(NavRoutes.PAYMENT_METHODS)
                        "settings" -> navController.navigate(NavRoutes.SETTINGS)
                    }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        },
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                HomeTopBar(
                    unreadNotificationCount = state.unreadNotificationCount,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSearchClick = { navController.navigate(NavRoutes.SEARCH) },
                    onNotificationsClick = { navController.navigate(NavRoutes.NOTIFICATIONS) },
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
                state.isLoading && state.featuredProducts.isEmpty() ->
                    LoadingIndicator(modifier = Modifier.padding(padding))

                state.errorMessage != null && state.featuredProducts.isEmpty() ->
                    ErrorMessage(
                        message = state.errorMessage ?: "Something went wrong",
                        modifier = Modifier.padding(padding),
                    )

                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(Modifier.height(8.dp))

                    BannerCarousel(
                        banners = state.banners,
                        onBannerClick = { banner ->
                            when (banner.targetRoute) {
                                "categories" -> navController.navigate(NavRoutes.CATEGORIES)
                                "cart" -> navController.navigate(NavRoutes.CART)
                                "orders" -> navController.navigate(NavRoutes.ORDERS)
                                "profile" -> navController.navigate(NavRoutes.PROFILE)
                                else -> Unit
                            }
                        },
                    )

                    Spacer(Modifier.height(24.dp))

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