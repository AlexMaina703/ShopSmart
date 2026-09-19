package com.shopsmart.app.features.home.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.Product
import com.shopsmart.app.features.home.domain.repository.HomeRepository

class GetFeaturedProductsUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): AppResult<List<Product>> = repository.getFeaturedProducts()
}