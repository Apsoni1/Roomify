package com.majorproject.roomify.feature.search.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.search.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<ProductDto>>(emptyList())
    val searchResults: StateFlow<List<ProductDto>> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private var searchJob: Job? = null

    fun searchProducts(query: String) {
        // Cancel any ongoing search
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _isSearching.value = true

            // Add a small delay to prevent rapid consecutive searches
            delay(300)

            try {
                val results = searchProductsUseCase(query)
                _searchResults.value = results
            } catch (e: Exception) {
                // Handle error
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }
}
