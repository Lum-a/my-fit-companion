package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "id")
    val userId: Int,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "firstName")
    val firstName: String? = null,
    @Json(name = "lastName")
    val lastName: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "heightCm")
    val height: Float? = null,
    @Json(name = "weightKg")
    val weight: Float? = null,
    @Json(name = "bodyFatPct")
    val bodyFat: Float? = null,
    @Json(name = "goalWeightKg")
    val goalWeight: Float?,
    @Json(name = "goalBodyFatPct")
    val goalBodyFat: Float?,
    @Json(name = "imageUrl")
    val imageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateProfileResponse(
    @Json(name = "id")
    val userId: Int,
    @Json(name = "name")
    val name: String?,
    @Json(name = "firstName")
    val firstName: String?,
    @Json(name = "lastName")
    val lastName: String?,
    @Json(name = "email")
    val email: String,
    @Json(name = "heightCm")
    val height: Float?,
    @Json(name = "weightKg")
    val weight: Float?,
    @Json(name = "bodyFatPct")
    val bodyFat: Float?,
    @Json(name = "goalWeightKg")
    val goalWeight: Float?,
    @Json(name = "goalBodyFatPct")
    val goalBodyFat: Float?,
    @Json(name = "imageUrl")
    val imageUrl: String?
)