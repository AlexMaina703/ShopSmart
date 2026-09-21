package com.shopsmart.app.features.home.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.PagedProducts
import com.shopsmart.app.features.home.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(
        categoryId: String? = null,
        search: String? = null,
        sortBy: String? = null,
        page: Int = 1,
        pageSize: Int = 20,
    ): AppResult<PagedProducts> = repository.getProducts(
        categoryId = categoryId,
        search = search,
        sortBy = sortBy,
        page = page,
        pageSize = pageSize,
    )
}