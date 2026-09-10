package com.shopsmart.app.core.network

import com.shopsmart.app.core.util.BaseResponse
import com.shopsmart.app.features.auth.data.remote.model.LoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService{
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<BaseResponse<Unit>>

    @POST("api/auth/login")
    suspend fun login(@Body request:LoginRequestDto): Response<BaseResponse<Unit>>

}