package com.example.myfitcompanion.api.model

data class ExerciseRequest(
    val name: String,
    val type: String,
    val duration: Int, // in minutes
    val caloriesBurned: Int
)
