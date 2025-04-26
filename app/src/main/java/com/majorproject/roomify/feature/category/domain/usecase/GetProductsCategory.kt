package com.majorproject.roomify.feature.furniture_list.domain.usecase


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.domain.repo.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> = repository.getCategories()
}



