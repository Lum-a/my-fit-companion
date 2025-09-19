package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainerResponse(
    @Json(name = "trainerId")
    val trainerId: Long,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "specialization")
    val specialization: String?,
    @Json(name = "contactInfo")
    val contactInfo: String?
)
