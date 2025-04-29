package com.majorproject.roomify.feature.home.presentaition.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.home.domain.usecase.FetchSecondItemsOfEachCategoryUseCase
import com.majorproject.roomify.feature.home.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val fetchSecondItemsOfEachCategoryUseCase: FetchSecondItemsOfEachCategoryUseCase
) : ViewModel() {

    private val _viewPagerCategories = MutableStateFlow<List<Category>>(emptyList())
    val viewPagerCategories: StateFlow<List<Category>> = _viewPagerCategories

    private val _gridCategories = MutableStateFlow<List<Category>>(emptyList())
    val gridCategories: StateFlow<List<Category>> = _gridCategories

    private val _secondItems = MutableStateFlow<List<ProductDto>>(emptyList())
    val secondItems: StateFlow<List<ProductDto>> = _secondItems

    // Loading state to track overall loading status
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Initialize with loading state true
        _isLoading.value = true
    }

    fun fetchViewPagerCategories() {
        viewModelScope.launch {
            try {
                val categories = getCategoriesUseCase(80)
                _viewPagerCategories.value = categories.reversed()
                checkLoadingState()
            } catch (e: Exception) {
                // Handle error
                checkLoadingState()
            }
        }
    }

    fun fetchGridCategories(limit: Int) {
        viewModelScope.launch {
            try {
                _gridCategories.value = getCategoriesUseCase(limit)
                checkLoadingState()
            } catch (e: Exception) {
                // Handle error
                checkLoadingState()
            }
        }
    }

    fun fetchSecondItemsOfEachCategory(limit: Int) {
        viewModelScope.launch {
            try {
                val result = fetchSecondItemsOfEachCategoryUseCase(limit)
                _secondItems.value = result
                checkLoadingState()
            } catch (e: Exception) {
                // Handle error
                checkLoadingState()
            }
        }
    }

    // Check if all data is loaded
    private fun checkLoadingState() {
        val isStillLoading = _viewPagerCategories.value.isEmpty() ||
                _gridCategories.value.isEmpty() ||
                _secondItems.value.isEmpty()
        _isLoading.value = isStillLoading
    }
}