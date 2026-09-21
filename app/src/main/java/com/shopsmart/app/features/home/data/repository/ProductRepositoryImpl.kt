package com.shopsmart.app.features.home.data.repository

import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.home.data.mappers.HomeMappers.toDomain
import com.shopsmart.app.features.home.domain.model.PagedProducts
import com.shopsmart.app.features.home.domain.model.Product
import com.shopsmart.app.features.home.domain.repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {

    override suspend fun getProductById(id: String): AppResult<Product> {
        return try {
            val response = RetrofitClient.apiService.getProductById(id)
            if (response.isSuccessful) {
                val dto = response.body()?.data
                    ?: return AppResult.Failure(Exception("Product not found"))
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(
                    Exception(response.errorBody()?.string() ?: "Failed to load product")
                )
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun getRelatedProducts(id: String): AppResult<List<Product>> {
        return try {
            val response = RetrofitClient.apiService.getRelatedProducts(id)
            if (response.isSuccessful) {
                AppResult.Success(
                    response.body()?.data?.map { it.toDomain() }.orEmpty()
                )
            } else {
                AppResult.Failure(
                    Exception(response.errorBody()?.string() ?: "Failed to load related products")
                )
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun getProducts(
        categoryId: String?,
        search: String?,
        sortBy: String?,
        page: Int,
        pageSize: Int,
    ): AppResult<PagedProducts> {
        return try {
            val response = RetrofitClient.apiService.getProducts(
                categoryId = categoryId,
                search = search,
                sortBy = sortBy,
                page = page,
                pageSize = pageSize,
            )
            if (response.isSuccessful) {
                val dto = response.body()?.data
                    ?: return AppResult.Failure(Exception("Empty response"))
                AppResult.Success(
                    PagedProducts(
                        products = dto.products.map { it.toDomain() },
                        total = dto.total,
                        page = dto.page,
                        pageSize = dto.pageSize,
                        totalPages = dto.totalPages,
                    )
                )
            } else {
                AppResult.Failure(
                    Exception(response.errorBody()?.string() ?: "Failed to load products")
                )
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }
}