package com.shopsmart.app.core.util

import com.google.gson.annotations.SerializedName
import com.shopsmart.app.features.auth.data.remote.model.UserDto

data class BaseResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("user") val user: UserDto? = null
)
