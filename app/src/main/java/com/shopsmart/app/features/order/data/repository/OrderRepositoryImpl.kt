package com.shopsmart.app.features.order.data.repository

import com.shopsmart.app.core.network.RetrofitClient
import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.data.mappers.OrderMappers.toDomain
import com.shopsmart.app.features.order.data.remote.model.AddAddressRequestDto
import com.shopsmart.app.features.order.data.remote.model.AddPaymentMethodRequestDto
import com.shopsmart.app.features.order.data.remote.model.PlaceOrderRequestDto
import com.shopsmart.app.features.order.domain.model.Address
import com.shopsmart.app.features.order.domain.model.Order
import com.shopsmart.app.features.order.domain.model.PaymentMethod
import com.shopsmart.app.features.order.domain.model.TrackingEvent
import com.shopsmart.app.features.order.domain.repository.OrderRepository

class OrderRepositoryImpl : OrderRepository {

    override suspend fun getAddresses(): AppResult<List<Address>> {
        return try {
            val r = RetrofitClient.apiService.getAddresses()
            if (r.isSuccessful) {
                AppResult.Success(r.body()?.data?.map { it.toDomain() }.orEmpty())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to load addresses"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun getPaymentMethods(): AppResult<List<PaymentMethod>> {
        return try {
            val r = RetrofitClient.apiService.getPaymentMethods()
            if (r.isSuccessful) {
                AppResult.Success(r.body()?.data?.map { it.toDomain() }.orEmpty())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to load payment methods"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun placeOrder(
        addressId: String,
        paymentMethodId: String,
        shippingMethod: String,
    ): AppResult<Order> {
        return try {
            val r = RetrofitClient.apiService.placeOrder(
                PlaceOrderRequestDto(addressId, paymentMethodId, shippingMethod)
            )
            if (r.isSuccessful) {
                val dto = r.body()?.data
                    ?: return AppResult.Failure(Exception("Order data missing"))
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to place order"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun getOrders(): AppResult<List<Order>> {
        return try {
            val r = RetrofitClient.apiService.getOrders()
            if (r.isSuccessful) {
                AppResult.Success(r.body()?.data?.map { it.toDomain() }.orEmpty())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to load orders"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun getOrderById(id: String): AppResult<Order> {
        return try {
            val r = RetrofitClient.apiService.getOrderById(id)
            if (r.isSuccessful) {
                val dto = r.body()?.data
                    ?: return AppResult.Failure(Exception("Order not found"))
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to load order"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun addAddress(
        label: String,
        fullName: String,
        phone: String,
        address: String,
        city: String,
        postalCode: String,
        isDefault: Boolean,
    ): AppResult<Address> {
        return try {
            val r = RetrofitClient.apiService.addAddress(
                AddAddressRequestDto(
                    label = label,
                    fullName = fullName,
                    phone = phone,
                    address = address,
                    city = city,
                    postalCode = postalCode,
                    isDefault = isDefault,
                )
            )
            if (r.isSuccessful) {
                val dto = r.body()?.data
                    ?: return AppResult.Failure(Exception("Address not returned"))
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to add address"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun deleteAddress(id: String): AppResult<Unit> {
        return try {
            val r = RetrofitClient.apiService.deleteAddress(id)
            if (r.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to delete address"))
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun addPaymentMethod(
        cardNumber: String,
        cardType: String,
        expiryMonth: Int,
        expiryYear: Int,
        cvv: String,
        isDefault: Boolean,
    ): AppResult<PaymentMethod> {
        return try {
            val r = RetrofitClient.apiService.addPaymentMethod(
                AddPaymentMethodRequestDto(
                    cardNumber = cardNumber,
                    cardType = cardType,
                    expiryMonth = expiryMonth,
                    expiryYear = expiryYear,
                    cvv = cvv,
                    isDefault = isDefault,
                )
            )
            if (r.isSuccessful) {
                val dto = r.body()?.data
                    ?: return AppResult.Failure(Exception("Payment not returned"))
                AppResult.Success(dto.toDomain())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to add payment method"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun deletePaymentMethod(id: String): AppResult<Unit> {
        return try {
            val r = RetrofitClient.apiService.deletePaymentMethod(id)
            if (r.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to delete payment"))
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }

    override suspend fun getOrderTracking(id: String): AppResult<List<TrackingEvent>> {
        return try {
            val r = RetrofitClient.apiService.trackOrder(id)
            if (r.isSuccessful) {
                AppResult.Success(r.body()?.data?.map { it.toDomain() }.orEmpty())
            } else {
                AppResult.Failure(Exception(r.errorBody()?.string() ?: "Failed to load tracking"))
            }
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }
}