package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExerciseRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "duration")
    val duration: Int, // in minutes
    @Json(name = "caloriesBurned")
    val caloriesBurned: Int
)
