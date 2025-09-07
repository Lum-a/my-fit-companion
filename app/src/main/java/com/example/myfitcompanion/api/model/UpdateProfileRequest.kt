package com.example.myfitcompanion.api.model

data class UpdateProfileRequest(
    val name: String? = null,
    val email: String? = null,
    val height: Float? = null,
    val weight: Float? = null,
    val bodyFat: Float? = null,
    val goal: String? = null
)

data class UpdateProfileResponse(
    val userId: Int,
    val name: String,
    val email: String,
    val height: Float?,
    val weight: Float?,
    val bodyFat: Float?,
    val goal: String?
)