package com.majorproject.roomify.feature.search.domain.repo


import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto

interface SearchRepository {
    suspend fun searchProducts(query: String): List<ProductDto>
}