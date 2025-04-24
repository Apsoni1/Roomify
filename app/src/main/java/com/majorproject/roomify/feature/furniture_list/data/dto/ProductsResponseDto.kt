package com.majorproject.roomify.feature.furniture_list.data.dto


data class ProductsResponseDto(
    val success: Boolean,
    val count: Int,
    val data: List<ProductDto>
)