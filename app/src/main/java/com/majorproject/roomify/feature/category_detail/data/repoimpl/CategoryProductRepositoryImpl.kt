package com.majorproject.roomify.feature.category_detail.data.repoimpl

import com.majorproject.roomify.feature.category_detail.data.datasource.CategoryProductRemoteDataSource
import com.majorproject.roomify.feature.category_detail.domain.repo.CategoryProductRepository
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import javax.inject.Inject

class CategoryProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryProductRemoteDataSource
) : CategoryProductRepository {

    override suspend fun getProductsByCategory(categoryName: String): List<ProductDto> {
        return remoteDataSource.fetchProductsByCategory(categoryName)
    }
}