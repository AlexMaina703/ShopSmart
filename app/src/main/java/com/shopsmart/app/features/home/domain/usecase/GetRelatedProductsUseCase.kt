package com.shopsmart.app.features.home.domain.usecase

import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.Product
import com.shopsmart.app.features.home.domain.repository.ProductRepository

class GetRelatedProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: String): AppResult<List<Product>> =
        repository.getRelatedProducts(id)
}