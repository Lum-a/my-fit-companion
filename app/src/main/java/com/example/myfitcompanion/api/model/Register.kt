package com.example.myfitcompanion.api.model

import com.example.myfitcompanion.model.entities.User

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val height: Float? = null,
    val weight: Float? = null,
    val bodyFat: Float? = null,
    val goal: String? = null
)

data class RegisterResponse(
    val message: String,
    val token: String,
    val user: User
)
