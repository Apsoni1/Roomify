package com.majorproject.roomify.feature.category.domain.usecase

import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.domain.repo.CategoryRepository
import javax.inject.Inject

class FetchSecondItemsOfEachCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(limit: Int): List<ProductDto> {
        return categoryRepository.fetchSecondItemsOfEachCategory(limit)
    }
}