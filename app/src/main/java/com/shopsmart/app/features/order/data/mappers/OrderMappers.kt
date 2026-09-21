package com.shopsmart.app.features.order.data.mappers


import com.shopsmart.app.features.order.data.remote.model.*
import com.shopsmart.app.features.order.domain.model.*

object OrderMappers {

    fun AddressDto.toDomain() = Address(
        id = id, label = label, fullName = fullName, phone = phone,
        address = address, city = city, postalCode = postalCode, isDefault = isDefault,
    )

    fun PaymentMethodDto.toDomain() = PaymentMethod(
        id = id,
        cardType = cardType,
        lastFour = lastFour,
        provider = provider,
        expiryMonth = expiryMonth,
        expiryYear = expiryYear,
        isDefault = isDefault,
    )

    fun OrderItemDto.toDomain() = OrderItem(
        id = id, productId = productId, productName = productName,
        productImage = productImage, quantity = quantity, price = price,
        color = color, storage = storage,
    )

    fun OrderDto.toDomain() = Order(
        id = id,
        orderNumber = orderNumber,
        status = status,
        items = items.map { it.toDomain() },
        subtotal = subtotal,
        shipping = shipping,
        total = total,
        shippingAddress = shippingAddress?.toDomain(),
        paymentMethod = paymentMethod?.toDomain(),
        tracking = tracking.map { it.toDomain() },
        createdAt = createdAt,
    )

    fun TrackingEventDto.toDomain() = TrackingEvent(
        id = id, status = status, location = location, note = note, timestamp = timestamp,
    )
}