package com.majorproject.roomify.feature.furniture_list.data.remote_data_source


import com.majorproject.roomify.core.di.ApiService
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductsResponseDto
import retrofit2.Response
import javax.inject.Inject

import javax.inject.Singleton

@Singleton
class ProductsRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchProducts(page: Int, limit: Int): Response<ProductsResponseDto> =
        apiService.fetchProductsPage(page, limit)
}

