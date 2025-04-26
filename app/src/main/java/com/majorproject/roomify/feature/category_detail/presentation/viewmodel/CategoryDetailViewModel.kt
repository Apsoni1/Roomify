package com.majorproject.roomify.feature.category_detail.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majorproject.roomify.feature.category_detail.domain.usecase.GetProductsByCategoryUseCase
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _categoryProducts = MutableStateFlow<List<ProductDto>>(emptyList())
    val categoryProducts: StateFlow<List<ProductDto>> = _categoryProducts.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchProductsByCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val products = getProductsByCategoryUseCase(categoryName)
                _categoryProducts.value = products

            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
                _categoryProducts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}