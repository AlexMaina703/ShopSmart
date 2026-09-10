package com.shopsmart.app.features.auth.data.mappers

import com.shopsmart.app.core.util.BaseResponse
import com.shopsmart.app.features.auth.data.remote.model.UserDto
import com.shopsmart.app.features.auth.domain.model.AuthResult

object AuthResultMapper {
    fun <T> mapToDomain(response: BaseResponse<T>): AuthResult {
        return AuthResult(
            success = response.success,
            message = response.message,
            token = response.token,
            user = response.user?.let { UserMapper.mapToDomain(it) }
        )
    }
}