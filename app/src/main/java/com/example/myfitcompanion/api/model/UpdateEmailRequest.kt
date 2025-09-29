package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateEmailRequest(
    @Json(name = "newEmail")
    val newEmail: String
)
