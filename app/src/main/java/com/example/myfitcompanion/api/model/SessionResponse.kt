package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "createdAt")
    val date: String, // ISO format recommended
    @Json(name = "duration")
    val duration: Int, // in minutes
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "imageUrl")
    val imageUrl: String? = null
)
