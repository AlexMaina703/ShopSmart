package com.shopsmart.app.core.network

import com.shopsmart.app.core.util.BaseResponse
import com.shopsmart.app.features.auth.data.remote.model.ForgotPasswordRequestDto
import com.shopsmart.app.features.auth.data.remote.model.LoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.RegisterRequestDto
import com.shopsmart.app.features.auth.data.remote.model.ResetPasswordRequestDto
import com.shopsmart.app.features.auth.data.remote.model.SocialLoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.UserDto
import com.shopsmart.app.features.home.data.remote.model.CategoryDto
import com.shopsmart.app.features.home.data.remote.model.PagedProductsDto
import com.shopsmart.app.features.home.data.remote.model.ProductDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<BaseResponse<Unit>>

    @POST("api/auth/login")
    suspend fun login(@Body request:LoginRequestDto): Response<BaseResponse<Unit>>


    @POST("api/auth/social")
    suspend fun socialLogin(
        @Body request: SocialLoginRequestDto
    ): Response<BaseResponse<Unit>>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequestDto
    ): Response<BaseResponse<Unit>>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequestDto
    ): Response<BaseResponse<Unit>>

    @GET("api/auth/profile")
    suspend fun getProfile(): Response<BaseResponse<UserDto>>




    @GET("api/products/featured")
    suspend fun getFeaturedProducts(): Response<BaseResponse<List<ProductDto>>>

    @GET("api/categories")
    suspend fun getCategories(): Response<BaseResponse<List<CategoryDto>>>

    @GET("api/products")
    suspend fun getProducts(
        @Query("categoryId") categoryId: String? = null,
        @Query("search") search: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
    ): Response<BaseResponse<PagedProductsDto>>


    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String,
    ): Response<BaseResponse<ProductDto>>

    @GET("api/products/{id}/related")
    suspend fun getRelatedProducts(
        @Path("id") id: String,
    ): Response<BaseResponse<List<ProductDto>>>

}