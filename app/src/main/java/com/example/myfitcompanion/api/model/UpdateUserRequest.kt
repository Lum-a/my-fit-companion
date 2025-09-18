package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateUserRequest(
    @Json(name = "firstName")
    val firstName: String? = null,
    @Json(name = "lastName")
    val lastName: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "role")
    val role: String? = null
)
