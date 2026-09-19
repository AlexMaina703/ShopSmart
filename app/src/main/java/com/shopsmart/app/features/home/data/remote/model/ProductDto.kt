package com.shopsmart.app.features.home.data.remote.model


import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val price: Double,
    @SerializedName("originalPrice") val originalPrice: Double? = null,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("images") val images: List<String> = emptyList(),
    @SerializedName("colors") val colors: List<String> = emptyList(),
    @SerializedName("storageOptions") val storageOptions: List<String> = emptyList(),
    @SerializedName("rating") val rating: Double = 0.0,
    @SerializedName("reviewCount") val reviewCount: Int = 0,
    @SerializedName("stock") val stock: Int = 0,
    @SerializedName("featured") val featured: Boolean = false,
    @SerializedName("discount") val discount: Double? = null,
    @SerializedName("discountPercent") val discountPercent: Int? = null,
    @SerializedName("specs") val specs: Map<String, Any?>? = null,
)