package com.example.myfitcompanion.api.model

data class MembershipResponse(
    val membershipId: Long,
    val userId: String,
    val planId: Long,
    val startDate: Long,
    val endDate: Long,
    val status: String
)
