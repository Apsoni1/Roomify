package com.majorproject.roomify.feature.furniture_list.domain.repo



import com.majorproject.roomify.feature.category.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}



