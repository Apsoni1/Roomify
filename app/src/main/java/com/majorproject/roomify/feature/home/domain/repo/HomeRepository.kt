package com.majorproject.roomify.feature.furniture_list.domain.repo



import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto

interface HomeRepository {
    suspend fun getCategories(limit:Int): List<Category>
    suspend fun fetchSecondItemsOfEachCategory(limit: Int): List<ProductDto>

}



