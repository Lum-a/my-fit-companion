package com.example.myfitcompanion.api.model

data class ExerciseResponse(
    val id: Int,
    val name: String,
    val type: String,
    val duration: Int, // in minutes
    val caloriesBurned: Int
)
