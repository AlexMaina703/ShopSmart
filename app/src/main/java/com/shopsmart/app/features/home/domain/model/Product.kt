package com.shopsmart.app.features.home.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val originalPrice: Double?,
    val categoryId: String,
    val brand: String?,
    val images: List<String>,
    val colors: List<String>,
    val storageOptions: List<String>,
    val rating: Double,
    val reviewCount: Int,
    val stock: Int,
    val specs: Map<String, String> = emptyMap(),
    val featured: Boolean,
    val discountPercent: Int?,
) {
    val primaryImage: String? get() = images.firstOrNull()
    val isInStock: Boolean get() = stock > 0
    val hasDiscount: Boolean get() = (discountPercent ?: 0) > 0
}