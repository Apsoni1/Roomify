package com.majorproject.roomify.feature.search.data.repo_impl

import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.search.data.data_source.SearchDataSource
import com.majorproject.roomify.feature.search.domain.repo.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchDataSource: SearchDataSource
) : SearchRepository {

    override suspend fun searchProducts(query: String): List<ProductDto> {
        return searchDataSource.searchProducts(query)
    }
}