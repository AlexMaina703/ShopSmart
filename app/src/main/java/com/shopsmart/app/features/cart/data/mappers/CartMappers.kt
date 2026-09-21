package com.shopsmart.app.features.cart.data.mappers

import com.shopsmart.app.features.cart.data.remote.model.CartDto
import com.shopsmart.app.features.cart.data.remote.model.CartItemDto
import com.shopsmart.app.features.cart.data.remote.model.CartProductDto
import com.shopsmart.app.features.cart.domain.model.Cart
import com.shopsmart.app.features.cart.domain.model.CartItem
import com.shopsmart.app.features.cart.domain.model.CartProduct

object CartMappers {

    fun CartDto.toDomain(): Cart = Cart(
        items = items.map { it.toDomain() },
        subtotal = subtotal,
        shipping = shipping,
        total = total,
        itemCount = itemCount,
    )

    fun CartItemDto.toDomain(): CartItem = CartItem(
        id = id,
        product = product.toDomain(),
        quantity = quantity,
        selectedColor = selectedColor,
        selectedStorage = selectedStorage,
    )

    fun CartProductDto.toDomain(): CartProduct = CartProduct(
        id = id,
        name = name,
        price = price,
        images = images,
    )
}