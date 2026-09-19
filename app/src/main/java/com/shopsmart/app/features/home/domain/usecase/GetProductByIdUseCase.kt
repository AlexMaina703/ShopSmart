package com.shopsmart.app.features.home.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.Product
import com.shopsmart.app.features.home.domain.repository.ProductRepository

class GetProductByIdUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: String): AppResult<Product> = repository.getProductById(id)
}