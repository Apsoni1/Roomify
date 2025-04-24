package com.majorproject.roomify.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.gson.annotations.SerializedName
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



data class DimensionsDto(
    val depth: Double,
    val width: Double,
    val height: Double
)

data class ProductDto(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    @SerializedName("wood_type")
    val woodType: String,
    val finish: String,
    val dimensions: DimensionsDto,
    val price: Double,
    val weight: Double,
    @SerializedName("image_path")
    val imagePath: String,
    val stock: Double,
    val sku: String,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val featured: Boolean,
    @SerializedName("discount_price")
    val discountPrice: Double,
    val tags: List<String>?
)

data class ProductsResponseDto(
    val success: Boolean,
    val count: Int,
    val data: List<ProductDto>
)



sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val exception: Throwable): Result<Nothing>()
}



@Singleton
class ProductRepository @Inject constructor(
    private val api: ApiService
) {
    fun pagingSource() = ProductPagingSource(api)
}


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repo: ProductRepository
): ViewModel() {

    val products = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 5),
        pagingSourceFactory = { repo.pagingSource() }
    ).flow
        .cachedIn(viewModelScope)
}

