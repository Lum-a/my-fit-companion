package com.example.myfitcompanion.model.trainer

data class TrainerResponse(
    val trainerId: Long,
    val name: String,
    val specialization: String?,
    val contactInfo: String?
)
