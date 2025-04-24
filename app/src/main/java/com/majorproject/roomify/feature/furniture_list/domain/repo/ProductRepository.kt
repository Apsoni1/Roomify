package com.majorproject.roomify.feature.furniture_list.domain.repo


import com.majorproject.roomify.feature.furniture_list.presentation.adapter.ProductPagingSource

interface ProductRepository {
    fun pagingSource(): ProductPagingSource
}

