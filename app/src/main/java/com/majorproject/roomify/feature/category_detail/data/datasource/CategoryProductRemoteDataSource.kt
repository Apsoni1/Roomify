package com.majorproject.roomify.feature.category_detail.data.datasource


import android.util.Log
import com.majorproject.roomify.core.di.ApiService
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import javax.inject.Inject

class CategoryProductRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchProductsByCategory(categoryName: String): List<ProductDto> {
        val response = apiService.fetchProductsPage(1, 100)
        val allProducts = response.body()?.data ?: emptyList()

        Log.d("CategoryFilter", "Searching for category: $categoryName")
        Log.d("CategoryFilter", "Available categories: ${allProducts.map { it.category }.distinct()}")

        return allProducts.filter {
            it.category.equals(categoryName, ignoreCase = true)
        }
    }
}