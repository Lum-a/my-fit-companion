package com.example.myfitcompanion.model.register

import com.example.myfitcompanion.model.User

data class RegisterResponse(
    val message: String,
    val token: String,
    val user: User
)
