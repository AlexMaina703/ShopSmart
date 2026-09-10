package com.shopsmart.app.features.auth.data.mappers

import com.shopsmart.app.features.auth.data.remote.model.UserDto
import com.shopsmart.app.features.auth.domain.model.User

object UserMapper {
    fun mapToDomain(userDto: UserDto): User {
        return User(
            id = userDto.id,
            email = userDto.email,
            fullName = userDto.fullName,
            phone = userDto.phone,
            avatarUrl = userDto.avatarUrl,
            provider = userDto.provider,
            emailVerifiedAt = userDto.emailVerified,
            createdAt = userDto.createdAt
        )
    }
}