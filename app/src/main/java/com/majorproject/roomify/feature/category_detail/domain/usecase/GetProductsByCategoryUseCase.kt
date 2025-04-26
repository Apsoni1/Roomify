package com.majorproject.roomify.feature.category_detail.domain.usecase

import com.majorproject.roomify.feature.category_detail.domain.repo.CategoryProductRepository
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: CategoryProductRepository
) {
    suspend operator fun invoke(categoryName: String): List<ProductDto> {
        return repository.getProductsByCategory(categoryName)
    }
}