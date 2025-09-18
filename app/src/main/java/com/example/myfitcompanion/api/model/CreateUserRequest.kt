package com.example.myfitcompanion.api.model

data class CreateUserRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: String
)
