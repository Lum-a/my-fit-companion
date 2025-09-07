package com.example.myfitcompanion.model.membership

data class MembershipResponse(
    val membershipId: Long,
    val userId: String,
    val planId: Long,
    val startDate: Long,
    val endDate: Long,
    val status: String
)
