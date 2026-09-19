package com.shopsmart.app.features.home.data.repository


import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.R
import com.shopsmart.app.features.home.data.mappers.HomeMappers.toDomain
import com.shopsmart.app.features.home.domain.model.Banner
import com.shopsmart.app.features.home.domain.model.Category
import com.shopsmart.app.features.home.domain.model.Product
import com.shopsmart.app.features.home.domain.repository.HomeRepository

class HomeRepositoryImpl : HomeRepository {

    override suspend fun getCategories(): AppResult<List<Category>> = try {
        val response = RetrofitClient.apiService.getCategories()
        if (response.isSuccessful) {
            val list = response.body()?.data?.map { it.toDomain() }.orEmpty()
            AppResult.Success(list)
        } else {
            AppResult.Failure(Exception(response.errorBody()?.string() ?: "Failed to load categories"))
        }
    } catch (e: Exception) {
        AppResult.Failure(e)
    }

    override suspend fun getFeaturedProducts(): AppResult<List<Product>> = try {
        val response = RetrofitClient.apiService.getFeaturedProducts()
        if (response.isSuccessful) {
            val list = response.body()?.data?.map { it.toDomain() }.orEmpty()
            AppResult.Success(list)
        } else {
            AppResult.Failure(Exception(response.errorBody()?.string() ?: "Failed to load products"))
        }
    } catch (e: Exception) {
        AppResult.Failure(e)
    }

    /**
     * Local banners. Swap this to a remote call when you add /api/banners.
     * Copy your 3 PNGs into app/src/main/res/drawable/ first.
     */
    override fun getLocalBanners(): List<Banner> = listOf(
        Banner(
            id = "b1",
            title = "Discover",
            subtitle = "Amazing Products",
            ctaText = "Shop Now",
            targetRoute = "categories",
            imageRes = R.drawable.banner_1,
        ),
        Banner(
            id = "b2",
            title = "Mega Sale",
            subtitle = "Up to 50% off",
            ctaText = "Grab Deals",
            targetRoute = "categories",
            imageRes = R.drawable.banner_2,
        ),
        Banner(
            id = "b3",
            title = "New Arrivals",
            subtitle = "Fresh tech just landed",
            ctaText = "Explore",
            targetRoute = "categories",
            imageRes = R.drawable.banner_3,
        ),
    )
}