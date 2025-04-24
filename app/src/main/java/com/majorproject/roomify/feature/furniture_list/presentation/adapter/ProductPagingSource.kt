package com.majorproject.roomify.feature.furniture_list.presentation.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductsResponseDto
import retrofit2.HttpException
import retrofit2.Response


class ProductPagingSource(
    private val fetchProducts: suspend (page: Int, limit: Int) -> Response<ProductsResponseDto>
) : PagingSource<Int, ProductDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDto> {
        val page = params.key ?: 1
        return try {
            val response = fetchProducts(page, params.loadSize)
            if (!response.isSuccessful) throw HttpException(response)

            val items = response.body()?.data ?: emptyList()
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductDto>): Int? {
        return state.anchorPosition?.let {
            val closestPage = state.closestPageToPosition(it)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}