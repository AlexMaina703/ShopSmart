package com.shopsmart.app.features.home.domain.repository

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.PagedProducts
import com.shopsmart.app.features.home.domain.model.Product

interface ProductRepository {
    suspend fun getProductById(id: String): AppResult<Product>
    suspend fun getRelatedProducts(id: String): AppResult<List<Product>>

    suspend fun getProducts(
        categoryId: String? = null,
        search: String? = null,
        sortBy: String? = null,
        page: Int = 1,
        pageSize: Int = 20,
    ): AppResult<PagedProducts>

}