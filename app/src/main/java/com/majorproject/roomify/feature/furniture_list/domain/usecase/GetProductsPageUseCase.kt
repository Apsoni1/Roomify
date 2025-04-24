package com.majorproject.roomify.feature.furniture_list.domain.usecase


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.domain.repo.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedProducts @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(pageSize: Int = 20, prefetch: Int = 5): Flow<PagingData<ProductDto>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = prefetch
            ),
            pagingSourceFactory = { repository.pagingSource() }
        ).flow
}

