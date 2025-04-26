package com.majorproject.roomify.feature.category.presentaition.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.category.domain.usecase.FetchSecondItemsOfEachCategoryUseCase
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val fetchSecondItemsOfEachCategoryUseCase: FetchSecondItemsOfEachCategoryUseCase
) : ViewModel() {

    private val _viewPagerCategories = MutableStateFlow<List<Category>>(emptyList())
    val viewPagerCategories: StateFlow<List<Category>> = _viewPagerCategories

    private val _gridCategories = MutableStateFlow<List<Category>>(emptyList())
    val gridCategories: StateFlow<List<Category>> = _gridCategories

    // Updated to use ProductDto instead of CategoryDto
    private val _secondItems = MutableStateFlow<List<ProductDto>>(emptyList())
    val secondItems: StateFlow<List<ProductDto>> = _secondItems

    fun fetchViewPagerCategories() {
        viewModelScope.launch {
            val categories = getCategoriesUseCase(80)
            _viewPagerCategories.value = categories.reversed()
        }
    }

    fun fetchGridCategories(limit: Int) {
        viewModelScope.launch {
            _gridCategories.value = getCategoriesUseCase(limit)
        }
    }

    fun fetchSecondItemsOfEachCategory(limit: Int) {
        viewModelScope.launch {
            try {
                val result = fetchSecondItemsOfEachCategoryUseCase(limit)
                _secondItems.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}