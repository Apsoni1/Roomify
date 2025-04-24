package com.majorproject.roomify.feature.furniture_list.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.domain.usecase.GetPagedProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getPagedProducts: GetPagedProducts
): ViewModel() {

    val products: StateFlow<PagingData<ProductDto>> =
        getPagedProducts()
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = PagingData.empty()
            )
}
