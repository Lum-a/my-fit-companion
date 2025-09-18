package com.example.myfitcompanion.api.model

data class MealRequest(
    val name: String,
    val calories: Int,
    val description: String?
)
