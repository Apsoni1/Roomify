package com.majorproject.roomify.feature.furniture_list.data.dto
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class DimensionsDto(
    val depth: Double,
    val width: Double,
    val height: Double
): Parcelable