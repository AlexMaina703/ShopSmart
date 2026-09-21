package com.shopsmart.app.features.order.data.remote.model

import com.google.gson.annotations.SerializedName

data class OrdersListDto(
    @SerializedName("orders") val orders: List<OrderDto> = emptyList(),
    @SerializedName("total") val total: Int = 0,
)