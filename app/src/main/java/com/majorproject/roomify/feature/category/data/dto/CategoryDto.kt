package com.majorproject.roomify.feature.category.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryDto(
    private val _name: String,
    val imageUrl: String
): Parcelable {
    val name: String
        get() = _name.lowercase().replaceFirstChar { it.uppercaseChar() }
}
