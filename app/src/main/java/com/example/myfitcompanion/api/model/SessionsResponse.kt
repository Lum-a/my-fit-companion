package com.example.myfitcompanion.api.model

data class SessionsResponse(
    val id: Int,
    val name: String,
    val date: String, // ISO format recommended
    val duration: Int, // in minutes
    val userId: Int
)
