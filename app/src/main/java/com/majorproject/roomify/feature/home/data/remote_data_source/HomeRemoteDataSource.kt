package com.majorproject.roomify.feature.home.data.remote_data_source



import com.majorproject.roomify.core.di.ApiService
import com.majorproject.roomify.feature.category.data.dto.CategoryDto
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchCategories(limit: Int): List<CategoryDto> {
        val response = apiService.fetchProductsPage(1, limit)
        val products = response.body()?.data ?: emptyList()

        return products.distinctBy { it.category }.map {
            CategoryDto(
                _name = it.category,
                imageUrl = it.imagePath
            )
        }
    }

    // New method to get the second item of each category
    suspend fun fetchSecondItemsOfEachCategory(limit: Int): List<ProductDto> {
        val response = apiService.fetchProductsPage(1, limit)
        val products = response.body()?.data ?: emptyList()

        val secondItems = products
            .groupBy { it.category }
            .mapNotNull { (_, items) ->
                if (items.size >= 2) {
                    items[1] // Return the complete second product of each category
                } else null
            }

        return secondItems
    }
}



