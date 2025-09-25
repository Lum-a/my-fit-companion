package com.example.myfitcompanion.model

enum class WorkoutType(val id: Int, val displayName: String) {
    CARDIO(1, "Cardio"),
    STRENGTH(2, "Strength"),
    BOXING(3, "Boxing"),
    CROSSFIT(4, "Crossfit"),
    YOGA(5, "Yoga")
}