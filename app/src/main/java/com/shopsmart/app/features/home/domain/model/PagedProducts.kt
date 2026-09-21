package com.shopsmart.app.features.home.domain.model


data class PagedProducts(
    val products: List<Product>,
    val total: Int,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int,
) {
    val hasMore: Boolean get() = page < totalPages
    val isEmpty: Boolean get() = products.isEmpty()
}