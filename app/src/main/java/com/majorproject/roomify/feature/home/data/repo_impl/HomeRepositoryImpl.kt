package com.majorproject.roomify.feature.furniture_list.data.repo_impl


import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.category.domain.model.toDomain
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.domain.repo.CategoryRepository
import com.majorproject.roomify.feature.furniture_list.domain.repo.HomeRepository
import com.majorproject.roomify.feature.home.data.remote_data_source.HomeRemoteDataSource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource
) : HomeRepository {
    override suspend fun getCategories(limit:Int): List<Category> {
        return remoteDataSource.fetchCategories(limit = limit).map { it.toDomain() }
    }
    override suspend fun fetchSecondItemsOfEachCategory(limit: Int): List<ProductDto> {
        return remoteDataSource.fetchSecondItemsOfEachCategory(limit)
    }
}




