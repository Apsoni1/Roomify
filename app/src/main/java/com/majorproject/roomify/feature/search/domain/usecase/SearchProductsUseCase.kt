package com.majorproject.roomify.feature.search.domain.usecase


import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.search.domain.repo.SearchRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String): List<ProductDto> {
        return searchRepository.searchProducts(query)
    }
}