package com.shopsmart.app.features.home.domain.repository


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.domain.model.Banner
import com.shopsmart.app.features.home.domain.model.Category
import com.shopsmart.app.features.home.domain.model.Product

interface HomeRepository {
    suspend fun getCategories(): AppResult<List<Category>>
    suspend fun getFeaturedProducts(): AppResult<List<Product>>
    fun getLocalBanners(): List<Banner>
}