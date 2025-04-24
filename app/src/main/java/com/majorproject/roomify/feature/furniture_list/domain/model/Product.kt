package com.majorproject.roomify.feature.furniture_list.domain.model


data class Product(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val woodType: String,
    val finish: String,
    val dimensions: Dimensions,
    val price: Double,
    val weight: Double,
    val imagePath: String,
    val stock: Double,
    val sku: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val featured: Boolean,
    val discountPrice: Double,
    val tags: List<String>?
)
