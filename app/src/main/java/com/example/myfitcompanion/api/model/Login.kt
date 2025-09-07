package com.example.myfitcompanion.api.model

import com.example.myfitcompanion.model.entities.User

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User
)