package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExerciseResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "type")
    val type: String? = null,
    @Json(name = "duration")
    val duration: Int? = null, // in minutes
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "videoId")
    val videoId: String,
)
