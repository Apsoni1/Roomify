package com.majorproject.roomify.feature.furniture_list.data.repo_impl


import com.majorproject.roomify.feature.category.data.dto.CategoryDto
import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.category.domain.model.toDomain
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.data.remote_data_source.CategoryRemoteDataSource
import com.majorproject.roomify.feature.furniture_list.domain.repo.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource
) : CategoryRepository {
    override suspend fun getCategories(limit:Int): List<Category> {
        return remoteDataSource.fetchCategories(limit = limit).map { it.toDomain() }
    }
    override suspend fun fetchSecondItemsOfEachCategory(limit: Int): List<ProductDto> {
        return remoteDataSource.fetchSecondItemsOfEachCategory(limit)
    }
}




