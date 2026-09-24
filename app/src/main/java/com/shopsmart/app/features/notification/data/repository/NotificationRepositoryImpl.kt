package com.shopsmart.app.features.notification.data.repository

import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.notification.data.mappers.NotificationMappers.toDomain
import com.shopsmart.app.features.notification.domain.model.AppNotification
import com.shopsmart.app.features.notification.domain.repository.NotificationRepository

class NotificationRepositoryImpl : NotificationRepository {

    override suspend fun getNotifications(): AppResult<List<AppNotification>> {
        return try {
            val r = RetrofitClient.apiService.getNotifications()
            if (r.isSuccessful) {
                AppResult.Success(r.body()?.data?.map { it.toDomain() }.orEmpty())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to load notifications"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun getUnreadCount(): AppResult<Int> {
        return try {
            val r = RetrofitClient.apiService.getUnreadCount()
            if (r.isSuccessful) AppResult.Success(r.body()?.data?.count ?: 0)
            else AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to load count"))
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun markAsRead(id: String): AppResult<Unit> {
        return try {
            val r = RetrofitClient.apiService.markNotificationRead(id)
            if (r.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to mark read"))
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun markAllAsRead(): AppResult<Unit> {
        return try {
            val r = RetrofitClient.apiService.markAllNotificationsRead()
            if (r.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to mark all read"))
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun delete(id: String): AppResult<Unit> {
        return try {
            val r = RetrofitClient.apiService.deleteNotification(id)
            if (r.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to delete"))
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }
}