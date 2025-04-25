package com.majorproject.roomify.feature.furniture_list.data.remote_data_source



import com.majorproject.roomify.core.di.ApiService
import com.majorproject.roomify.feature.category.data.dto.CategoryDto
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchCategories(): List<CategoryDto> {
        val response = apiService.fetchProductsPage(1, 100) // Or paginate accordingly
        val products = response.body()?.data ?: emptyList()

        val categoryMap = products.distinctBy { it.category }.map {
            CategoryDto(
                name = it.category,
                imageUrl = it.imagePath // Or your logic for category-image mapping
            )
        }

        return categoryMap
    }
}


