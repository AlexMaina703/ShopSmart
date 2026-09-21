package com.shopsmart.app.features.home.domain.usecase


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.Category
import com.shopsmart.app.features.home.domain.repository.HomeRepository

class GetCategoriesUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): AppResult<List<Category>> = repository.getCategories()
}