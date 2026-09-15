package com.shopsmart.app.features.auth.data.repository

import com.shopsmart.app.core.datastore.DataStoreManager
import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.network.UnauthorizedException
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.auth.data.mappers.AuthResultMapper
import com.shopsmart.app.features.auth.data.remote.model.LoginRequestDto
import com.shopsmart.app.features.auth.data.remote.model.RegisterRequestDto
import com.shopsmart.app.features.auth.data.remote.model.SocialLoginRequestDto
import com.shopsmart.app.features.auth.domain.model.AuthProvider
import com.shopsmart.app.features.auth.domain.model.AuthResult
import com.shopsmart.app.features.auth.domain.model.User
import com.shopsmart.app.features.auth.domain.repository.AuthRepository


class AuthRepositoryImpl(private val dataStore: DataStoreManager) : AuthRepository{
    override suspend fun login(email: String, password: String): AppResult<AuthResult> {
        return try {
            val response = RetrofitClient.apiService.login(LoginRequestDto(email, password))
            if (response.isSuccessful) {
                response.body()?.let { baseResponse ->
                    val authResult = AuthResultMapper.mapToDomain(baseResponse)
                    authResult.token?.let { token ->
                        authResult.user?.let { user ->
                            dataStore.saveAuthData(token, user.id, user.email, user.fullName)
                            RetrofitClient.setToken(token)
                        }
                    }
                    AppResult.Success(authResult)
                } ?: AppResult.Failure(Exception("Empty response"))
            } else {
                val error = response.errorBody()?.string() ?: "Login failed"
                AppResult.Failure(Exception(error))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun register(fullName: String, email: String, password: String, phone: String): AppResult<AuthResult> {
        return try {
            val response = RetrofitClient.apiService.register(RegisterRequestDto(fullName, email, password, phone))
            if (response.isSuccessful) {
                response.body()?.let { baseResponse ->
                    val authResult = AuthResultMapper.mapToDomain(baseResponse)
                    authResult.token?.let { token ->
                        authResult.user?.let { user ->
                            dataStore.saveAuthData(token, user.id, user.email, user.fullName)
                            RetrofitClient.setToken(token)
                        }
                    }
                    AppResult.Success(authResult)
                } ?: AppResult.Failure(Exception("Empty response"))
            } else {
                val error = response.errorBody()?.string() ?: "Registration failed"
                AppResult.Failure(Exception(error))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }
    override suspend fun validateToken(): AppResult<User> {
        return try {
            val token = dataStore.getToken()
            if (token.isNullOrEmpty()) return AppResult.Failure(Exception("No token"))
            // In a real app, call a profile endpoint:
            // val response = RetrofitClient.apiService.getProfile()
            // return response.body()?.data?.let { AppResult.Success(it) } ?: AppResult.Failure(...)
            // For now, we return failure (stub)
            AppResult.Failure(Exception("Validation not implemented"))
        } catch (e: Exception) {
            if (e is UnauthorizedException) {
                dataStore.clearAuthData()
                RetrofitClient.setToken(null)
            }
            AppResult.Failure(e)
        }
    }

    override suspend fun loadToken(): AppResult<Unit> {
        return try {
            RetrofitClient.setToken(dataStore.getToken())
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun logout(): AppResult<Unit> {
        return try {
            dataStore.clearAuthData()
            RetrofitClient.setToken(null)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun isLoggedIn(): AppResult<Boolean> {
        return try {
            AppResult.Success(dataStore.getToken() != null)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun socialLogin(
        provider: AuthProvider,
        accessToken: String
    ): AppResult<AuthResult> = runAuthCall {
        val response = RetrofitClient.apiService.socialLogin(
            SocialLoginRequestDto(
                provider = provider.value,
                token = accessToken,          // ← renamed
            )
        )
        if (!response.isSuccessful) {
            val body = response.errorBody()?.string()
            return@runAuthCall AppResult.Failure(
                Exception(body ?: "${provider.value} sign-in failed")
            )
        }
        val base = response.body()
            ?: return@runAuthCall AppResult.Failure(Exception("Empty response"))
        val authResult = AuthResultMapper.mapToDomain(base)
        authResult.token?.let { token ->
            authResult.user?.let { user ->
                dataStore.saveAuthData(token, user.id, user.email, user.fullName)
                RetrofitClient.setToken(token)
            }
        }
        AppResult.Success(authResult)
    }

    // Private helper – keep at the bottom of AuthRepositoryImpl
    private inline fun <T> runAuthCall(block: () -> AppResult<T>): AppResult<T> =
        try { block() } catch (e: Exception) { AppResult.Failure(e) }

}