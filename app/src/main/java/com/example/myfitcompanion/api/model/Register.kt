package com.example.myfitcompanion.api.model

import com.example.myfitcompanion.model.entities.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "height")
    val height: Float? = null,
    @Json(name = "weight")
    val weight: Float? = null,
    @Json(name = "bodyFat")
    val bodyFat: Float? = null,
    @Json(name = "goal")
    val goal: String? = null
)
@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "token")
    val token: String,
    @Json(name = "user")
    val user: User
)
