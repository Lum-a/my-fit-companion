package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkoutRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "date")
    val date: Long, // millis
    @Json(name = "duration")
    val duration: Int, // in minutes
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "imageUrl")
    val imageUrl: String?
)
