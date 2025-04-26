package com.majorproject.roomify.feature.furniture_list.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ProductDto(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    @SerializedName("wood_type")
    val woodType: String,
    val finish: String,
    val dimensions: DimensionsDto,
    val price: Double,
    val weight: Double,
    @SerializedName("image_path")
    val imagePath: String,
    val stock: Double,
    val sku: String,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val featured: Boolean,
    @SerializedName("discount_price")
    val discountPrice: Double,
    val tags: List<String>?
) : Parcelable
