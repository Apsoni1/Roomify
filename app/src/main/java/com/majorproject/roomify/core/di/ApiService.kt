package com.majorproject.roomify.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.gson.annotations.SerializedName
import com.majorproject.roomify.feature.furniture_list.data.dto.DimensionsDto
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductsResponseDto
import com.majorproject.roomify.feature.furniture_list.presentation.adapter.ProductPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface ApiService {
    @GET("v1/products")
    suspend fun fetchProductsPage(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ProductsResponseDto>
}










sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val exception: Throwable): Result<Nothing>()
}






