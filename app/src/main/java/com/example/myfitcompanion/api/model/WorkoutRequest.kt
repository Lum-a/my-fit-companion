package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkoutRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?
)
