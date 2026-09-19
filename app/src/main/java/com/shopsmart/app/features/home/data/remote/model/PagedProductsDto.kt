package com.shopsmart.app.features.home.data.remote.model

import com.google.gson.annotations.SerializedName

data class PagedProductsDto(
    @SerializedName("products") val products: List<ProductDto> = emptyList(),
    @SerializedName("total") val total: Int = 0,
    @SerializedName("page") val page: Int = 1,
    @SerializedName("pageSize") val pageSize: Int = 20,
    @SerializedName("totalPages") val totalPages: Int = 1, )