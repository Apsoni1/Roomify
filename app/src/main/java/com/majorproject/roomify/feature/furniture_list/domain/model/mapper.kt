package com.majorproject.roomify.feature.furniture_list.domain.model

// mapper.kt
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.furniture_list.domain.model.Dimensions
import com.majorproject.roomify.feature.furniture_list.domain.model.Product

fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        name = name,
        category = category,
        description = description,
        woodType = woodType,
        finish = finish,
        dimensions = Dimensions(
            width = dimensions.width,
            height = dimensions.height,
            depth = dimensions.depth
        ),
        price = price,
        weight = weight,
        imagePath = imagePath,
        stock = stock,
        sku = sku,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        featured = featured,
        discountPrice = discountPrice,
        tags = tags
    )

