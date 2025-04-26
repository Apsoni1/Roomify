package com.majorproject.roomify.feature.category.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    private val _name: String,
    val imagePath: String
) : Parcelable {
    val name: String
        get() = _name.lowercase().replaceFirstChar { it.uppercaseChar() }
}
