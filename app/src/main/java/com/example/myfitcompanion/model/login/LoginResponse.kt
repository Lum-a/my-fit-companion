package com.example.myfitcompanion.model.login

import com.example.myfitcompanion.model.User

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User
)