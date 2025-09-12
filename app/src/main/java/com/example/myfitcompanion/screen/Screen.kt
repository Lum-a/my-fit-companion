package com.example.myfitcompanion.screen

import com.example.myfitcompanion.api.model.UserResponse
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Splash: Screen()

    @Serializable
    data object Login: Screen()

    @Serializable
    data object Register: Screen()

    @Serializable
    data class Home(val userResponse: UserResponse): Screen()

    @Serializable
    data object Profile: Screen()

    @Serializable
    data object Classes: Screen()

    @Serializable
    data object Trainer: Screen()

    @Serializable
    data object Plan: Screen()
}