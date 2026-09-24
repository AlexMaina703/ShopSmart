package com.shopsmart.app.features.wishlist.data.mappers

import com.shopsmart.app.features.wishlist.data.model.WishlistItemDto
import com.shopsmart.app.features.wishlist.data.model.WishlistProductDto
import com.shopsmart.app.features.wishlist.domain.model.WishlistItem
import com.shopsmart.app.features.wishlist.domain.model.WishlistProduct

object WishlistMappers {

    fun WishlistProductDto.toDomain() = WishlistProduct(
        id = id,
        name = name,
        price = price,
        images = images,
        rating = rating,
    )

    fun WishlistItemDto.toDomain() = WishlistItem(
        id = id,
        product = product.toDomain(),
        addedAt = addedAt,
    )
}