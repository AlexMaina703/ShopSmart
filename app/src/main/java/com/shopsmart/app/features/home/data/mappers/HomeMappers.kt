package com.shopsmart.app.features.home.data.mappers


import com.shopsmart.app.features.home.data.remote.model.CategoryDto
import com.shopsmart.app.features.home.data.remote.model.ProductDto
import com.shopsmart.app.features.home.domain.model.Category
import com.shopsmart.app.features.home.domain.model.Product

object HomeMappers {

    fun ProductDto.toDomain(): Product = Product(
        id = id,
        name = name,
        description = description,
        price = price,
        originalPrice = originalPrice,
        categoryId = categoryId,
        brand = brand,
        images = images,
        colors = colors,
        storageOptions = storageOptions,
        rating = rating,
        reviewCount = reviewCount,
        stock = stock,
        featured = featured,
        discountPercent = discountPercent,
        specs = specs?.mapValues { it.value?.toString() ?: "" }.orEmpty(),

    )

    fun CategoryDto.toDomain(): Category = Category(
        id = id,
        name = name,
        description = description,
        image = image,
    )
}