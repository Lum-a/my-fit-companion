package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordUpdatedModel(
    @Json(name = "oldPassword") val oldPassword: String,
    @Json(name = "newPassword") val newPassword: String
)