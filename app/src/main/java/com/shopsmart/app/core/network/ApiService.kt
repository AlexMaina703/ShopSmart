package com.shopsmart.app.core.network

import com.shopsmart.app.core.util.BaseResponse
import com.shopsmart.app.features.auth.data.remote.model.ForgotPasswordRequestDto
import com.shopsmart.app.features.auth.data.remote.model.LoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.RegisterRequestDto
import com.shopsmart.app.features.auth.data.remote.model.ResetPasswordRequestDto
import com.shopsmart.app.features.auth.data.remote.model.SocialLoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.UserDto
import com.shopsmart.app.features.cart.data.remote.model.AddToCartRequestDto
import com.shopsmart.app.features.cart.data.remote.model.CartDto
import com.shopsmart.app.features.cart.data.remote.model.UpdateCartQuantityRequestDto
import com.shopsmart.app.features.home.data.remote.model.CategoryDto
import com.shopsmart.app.features.home.data.remote.model.PagedProductsDto
import com.shopsmart.app.features.home.data.remote.model.ProductDto
import com.shopsmart.app.features.order.data.remote.model.AddAddressRequestDto
import com.shopsmart.app.features.order.data.remote.model.AddPaymentMethodRequestDto
import com.shopsmart.app.features.order.data.remote.model.AddressDto
import com.shopsmart.app.features.order.data.remote.model.OrderDto
import com.shopsmart.app.features.order.data.remote.model.OrdersListDto
import com.shopsmart.app.features.order.data.remote.model.PaymentMethodDto
import com.shopsmart.app.features.order.data.remote.model.PlaceOrderRequestDto
import com.shopsmart.app.features.order.data.remote.model.TrackingEventDto
import com.shopsmart.app.features.profile.data.remote.model.ProfileResponseDto
import com.shopsmart.app.features.profile.data.remote.model.UpdateProfileRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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



    // Cart
    @GET("api/cart")
    suspend fun getCart(): Response<BaseResponse<CartDto>>

    @POST("api/cart")
    suspend fun addToCart(@Body request: AddToCartRequestDto): Response<BaseResponse<CartDto>>

    @PUT("api/cart/{id}")
    suspend fun updateCartItem(
        @Path("id") id: String,
        @Body request: UpdateCartQuantityRequestDto,
    ): Response<BaseResponse<CartDto>>

    @DELETE("api/cart/{id}")
    suspend fun removeCartItem(@Path("id") id: String): Response<BaseResponse<CartDto>>

    @DELETE("api/cart")
    suspend fun clearCart(): Response<BaseResponse<Unit>>



    // User
    @GET("api/user/addresses")
    suspend fun getAddresses(): Response<BaseResponse<List<AddressDto>>>

    @GET("api/user/payment-methods")
    suspend fun getPaymentMethods(): Response<BaseResponse<List<PaymentMethodDto>>>

    // Orders
    @POST("api/orders")
    suspend fun placeOrder(@Body request: PlaceOrderRequestDto): Response<BaseResponse<OrderDto>>

    @GET("api/orders")
    suspend fun getOrders(): Response<BaseResponse<List<OrderDto>>>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<BaseResponse<OrderDto>>


    @POST("api/user/addresses")
    suspend fun addAddress(
        @Body request: AddAddressRequestDto,
    ): Response<BaseResponse<AddressDto>>

    @DELETE("api/user/addresses/{id}")
    suspend fun deleteAddress(@Path("id") id: String): Response<BaseResponse<Unit>>


    @POST("api/user/payment-methods")
    suspend fun addPaymentMethod(
        @Body request: AddPaymentMethodRequestDto,
    ): Response<BaseResponse<PaymentMethodDto>>

    @DELETE("api/user/payment-methods/{id}")
    suspend fun deletePaymentMethod(@Path("id") id: String): Response<BaseResponse<Unit>>


    @GET("api/orders/{id}/track")
    suspend fun trackOrder(@Path("id") id: String): Response<BaseResponse<List<TrackingEventDto>>>


    // Profile
    @GET("api/user/profile")
    suspend fun getProfileFull(): Response<BaseResponse<ProfileResponseDto>>

    @PUT("api/user/profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto,
    ): Response<BaseResponse<ProfileResponseDto>>
}