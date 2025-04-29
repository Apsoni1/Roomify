package com.majorproject.roomify.feature.search.data.data_source

import com.majorproject.roomify.core.di.ApiService
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import javax.inject.Inject

class SearchDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun searchProducts(query: String): List<ProductDto> {
        // Since we don't have a dedicated search endpoint, we'll fetch products
        // and filter them locally
        val response = apiService.fetchProductsPage(1, 100) // Fetch a reasonable number of products
        val products = response.body()?.data ?: emptyList()

        // Filter products based on query
        return products.filter { product ->
            product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)
        }
    }
}