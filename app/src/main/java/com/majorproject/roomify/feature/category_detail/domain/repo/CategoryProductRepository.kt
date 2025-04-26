package com.majorproject.roomify.feature.category_detail.domain.repo

import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto

interface CategoryProductRepository {
    suspend fun getProductsByCategory(categoryName: String): List<ProductDto>
}
