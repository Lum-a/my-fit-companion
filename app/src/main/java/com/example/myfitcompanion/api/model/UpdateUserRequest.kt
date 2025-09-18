package com.example.myfitcompanion.api.model

data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val role: String? = null,
)
