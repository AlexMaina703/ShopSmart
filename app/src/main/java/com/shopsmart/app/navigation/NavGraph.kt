package com.shopsmart.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.shopsmart.app.features.auth.presentation.authScreens.ForgotPasswordScreen
import com.shopsmart.app.features.auth.presentation.authScreens.LoginScreen
import com.shopsmart.app.features.auth.presentation.authScreens.OnboardingScreen
import com.shopsmart.app.features.auth.presentation.authScreens.RegisterScreen
import com.shopsmart.app.features.auth.presentation.authScreens.ResetPasswordScreen
import com.shopsmart.app.features.auth.presentation.authScreens.SplashScreen
import com.shopsmart.app.features.cart.presentation.screens.CartScreen
import com.shopsmart.app.features.home.presentation.screens.CategoriesScreen
import com.shopsmart.app.features.home.presentation.screens.HomeScreen
import com.shopsmart.app.features.home.presentation.screens.ProductDetailScreen
import com.shopsmart.app.features.home.presentation.screens.ProductListScreen
import com.shopsmart.app.features.notification.presentation.screens.NotificationsScreen
import com.shopsmart.app.features.order.presentation.screens.AddAddressScreen
import com.shopsmart.app.features.order.presentation.screens.AddPaymentMethodScreen
import com.shopsmart.app.features.order.presentation.screens.AddressesScreen
import com.shopsmart.app.features.order.presentation.screens.CheckoutScreen
import com.shopsmart.app.features.order.presentation.screens.OrderDetailScreen
import com.shopsmart.app.features.order.presentation.screens.OrderSuccessScreen
import com.shopsmart.app.features.order.presentation.screens.OrdersScreen
import com.shopsmart.app.features.order.presentation.screens.PaymentMethodsScreen
import com.shopsmart.app.features.order.presentation.screens.TrackOrderScreen
import com.shopsmart.app.features.profile.presentation.screens.EditProfileScreen
import com.shopsmart.app.features.profile.presentation.screens.ProfileScreen
import com.shopsmart.app.features.search.presentation.screens.SearchScreen
import com.shopsmart.app.features.settings.presentation.screens.SettingsScreen
import com.shopsmart.app.features.wishlist.presentation.screens.WishlistScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavRoutes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(navController)
        }
        composable(NavRoutes.ONBOARDING) {
            OnboardingScreen(navController)
        }
        composable(NavRoutes.LOGIN) {
            LoginScreen(navController)
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController)
        }
        composable(NavRoutes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(NavRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(navController)
        }
        composable(NavRoutes.RESET_PASSWORD) {
            ResetPasswordScreen(navController)
        }

        composable(
            route = NavRoutes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("productId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId").orEmpty()
            ProductDetailScreen(
                productId = productId,
                navController = navController,
            )
        }


        composable(NavRoutes.CATEGORIES) {
            CategoriesScreen(navController)
        }

        composable(
            route = NavRoutes.PRODUCT_LIST,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("categoryName") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId").orEmpty()
            val categoryName = java.net.URLDecoder.decode(
                backStackEntry.arguments?.getString("categoryName").orEmpty(),
                "UTF-8",
            )
            ProductListScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                navController = navController,
            )
        }


        composable(NavRoutes.CART) {
            CartScreen(navController)
        }

        composable(NavRoutes.CHECKOUT) {
            CheckoutScreen(navController)
        }

        composable(
            route = NavRoutes.ORDER_SUCCESS,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId").orEmpty()
            OrderSuccessScreen(orderId = orderId, navController = navController)
        }

        composable(NavRoutes.ORDERS) {
            OrdersScreen(navController)
        }



        composable(NavRoutes.ADD_ADDRESS) {
            AddAddressScreen(navController)
        }

        composable(NavRoutes.ADD_PAYMENT_METHOD) {
            AddPaymentMethodScreen(navController)
        }

        composable(
            route = NavRoutes.ORDER_DETAIL,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId").orEmpty()
            OrderDetailScreen(orderId = orderId, navController = navController)
        }

        composable(
            route = NavRoutes.TRACK_ORDER,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId").orEmpty()
            TrackOrderScreen(orderId = orderId, navController = navController)
        }

        composable(NavRoutes.PROFILE) {
            ProfileScreen(navController)
        }

        composable(NavRoutes.EDIT_PROFILE) {
            EditProfileScreen(navController)
        }

        composable(NavRoutes.WISHLIST) {
            WishlistScreen(navController)
        }


        composable(NavRoutes.ADDRESSES) { AddressesScreen(navController) }
        composable(NavRoutes.PAYMENT_METHODS) { PaymentMethodsScreen(navController) }
        composable(NavRoutes.WISHLIST) { WishlistScreen(navController) }
        composable(NavRoutes.NOTIFICATIONS) { NotificationsScreen(navController) }
        composable(NavRoutes.SETTINGS) { SettingsScreen(navController) }

        composable(NavRoutes.SEARCH) {
            SearchScreen(navController)
        }
    }
}