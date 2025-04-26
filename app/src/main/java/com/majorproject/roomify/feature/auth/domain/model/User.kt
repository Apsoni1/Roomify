package com.majorproject.roomify.feature.auth.domain.model


data class User(
    val id: String,
    val email: String,
    val displayName: String? = null
)