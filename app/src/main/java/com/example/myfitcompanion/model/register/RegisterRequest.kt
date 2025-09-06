package com.example.myfitcompanion.model.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val height: Float? = null,
    val weight: Float? = null,
    val bodyFat: Float? = null,
    val goal: String? = null
)
