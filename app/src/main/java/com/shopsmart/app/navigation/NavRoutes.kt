package com.shopsmart.app.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val RESET_PASSWORD = "reset_password"

    const val HOME = "home"

    const val PRODUCT_DETAIL = "product_detail/{productId}"

    fun productDetail(productId: String) = "product_detail/$productId"

    const val CATEGORIES = "categories"

    const val PRODUCT_LIST = "product_list/{categoryId}/{categoryName}"
    fun productList(categoryId: String, categoryName: String): String {
        // URL-encode the category name in case it has spaces or special chars
        val encoded = java.net.URLEncoder.encode(categoryName, "UTF-8")
        return "product_list/$categoryId/$encoded"
    }


    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val ORDER_SUCCESS = "order_success/{orderId}"
    fun orderSuccess(orderId: String) = "order_success/$orderId"

    const val ORDERS = "orders"
    const val ORDER_DETAIL = "order_detail/{orderId}"
    fun orderDetail(orderId: String) = "order_detail/$orderId"


    const val ADD_ADDRESS = "add_address"
    const val ADD_PAYMENT_METHOD = "add_payment_method"

    const val TRACK_ORDER = "track_order/{orderId}"
    fun trackOrder(orderId: String) = "track_order/$orderId"
}