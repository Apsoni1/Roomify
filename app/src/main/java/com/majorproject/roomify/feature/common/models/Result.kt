package com.majorproject.roomify.feature.common.models

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val exception: Throwable): Result<Nothing>()
}