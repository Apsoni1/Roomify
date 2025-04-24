package com.majorproject.roomify.feature.furniture_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.majorproject.roomify.core.di.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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