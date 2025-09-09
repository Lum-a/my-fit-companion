package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "role")
    val role: String,
    @Json(name = "height")
    val height: Float? = null,
    @Json(name = "weight")
    val weight: Float? = null,
    @Json(name = "bodyFat")
    val bodyFat: Float? = null,
    @Json(name = "goal")
    val goal: String? = null,
    @Json(name = "createdAt")
    val createdAt: String
)
