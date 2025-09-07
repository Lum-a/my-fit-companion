package com.example.myfitcompanion.api.model

data class TrainerResponse(
    val trainerId: Long,
    val name: String,
    val specialization: String?,
    val contactInfo: String?
)
