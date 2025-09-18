package com.example.myfitcompanion.api.model

data class MealsResponse(
    val id: Int,
    val name: String,
    val calories: Int,
    val description: String?
)
