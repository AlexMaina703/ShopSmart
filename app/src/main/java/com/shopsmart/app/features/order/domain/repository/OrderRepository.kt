package com.shopsmart.app.features.order.domain.repository


import com.shopsmart.app.core.util.AppResult
import com.shopsmart.app.features.order.domain.model.Address
import com.shopsmart.app.features.order.domain.model.Order
import com.shopsmart.app.features.order.domain.model.PaymentMethod
import com.shopsmart.app.features.order.domain.model.TrackingEvent

interface OrderRepository {
    suspend fun getAddresses(): AppResult<List<Address>>
    suspend fun getPaymentMethods(): AppResult<List<PaymentMethod>>
    suspend fun placeOrder(
        addressId: String,
        paymentMethodId: String,
        shippingMethod: String = "standard",
    ): AppResult<Order>
    suspend fun getOrders(): AppResult<List<Order>>
    suspend fun getOrderById(id: String): AppResult<Order>

    suspend fun addAddress(
        label: String,
        fullName: String,
        phone: String,
        address: String,
        city: String,
        postalCode: String,
        isDefault: Boolean,
    ): AppResult<Address>

    suspend fun deleteAddress(id: String): AppResult<Unit>



    suspend fun addPaymentMethod(
        cardNumber: String,
        cardType: String,
        expiryMonth: Int,
        expiryYear: Int,
        cvv: String,
        isDefault: Boolean,
    ): AppResult<PaymentMethod>

    suspend fun deletePaymentMethod(id: String): AppResult<Unit>

    suspend fun getOrderTracking(id: String): AppResult<List<TrackingEvent>>
}