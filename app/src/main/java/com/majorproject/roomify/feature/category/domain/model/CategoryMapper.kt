package com.majorproject.roomify.feature.category.domain.model


import com.majorproject.roomify.feature.category.data.dto.CategoryDto
import com.majorproject.roomify.feature.category.domain.model.Category

fun CategoryDto.toDomain(): Category = Category(
    name = name,
    imagePath = imageUrl
)
