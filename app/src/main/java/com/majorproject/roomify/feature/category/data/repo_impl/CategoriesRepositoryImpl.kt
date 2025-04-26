package com.majorproject.roomify.feature.furniture_list.data.repo_impl


import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.category.domain.model.toDomain
import com.majorproject.roomify.feature.furniture_list.data.remote_data_source.CategoryRemoteDataSource
import com.majorproject.roomify.feature.furniture_list.domain.repo.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> {
        return remoteDataSource.fetchCategories().map { it.toDomain() }
    }
}




