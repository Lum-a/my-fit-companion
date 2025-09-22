package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "firstName")
    val firstName: String? = null,
    @Json(name = "lastName")
    val lastName: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "height")
    val height: Float? = null,
    @Json(name = "weight")
    val weight: Float? = null,
    @Json(name = "bodyFat")
    val bodyFat: Float? = null,
    @Json(name = "goal")
    val goal: String? = null,
    @Json(name = "photoUrl")
    val imageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateProfileResponse(
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "height")
    val height: Float?,
    @Json(name = "weight")
    val weight: Float?,
    @Json(name = "bodyFat")
    val bodyFat: Float?,
    @Json(name = "goal")
    val goal: String?,
    @Json(name = "photoUrl")
    val imageUrl: String?
)