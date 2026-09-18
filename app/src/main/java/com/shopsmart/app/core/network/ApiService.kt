package com.shopsmart.app.core.network

import com.shopsmart.app.core.util.BaseResponse
import com.shopsmart.app.features.auth.data.remote.model.ForgotPasswordRequestDto
import com.shopsmart.app.features.auth.data.remote.model.LoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.RegisterRequestDto
import com.shopsmart.app.features.auth.data.remote.model.ResetPasswordRequestDto
import com.shopsmart.app.features.auth.data.remote.model.SocialLoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
}