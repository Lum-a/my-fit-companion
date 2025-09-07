package com.example.myfitcompanion.screen

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Splash: Screen()

    @Serializable
    data object Login: Screen()

    @Serializable
    data object Register: Screen()

    @Serializable
    data object Home: Screen()

    @Serializable
    data object Profile: Screen()

    @Serializable
    data object Classes: Screen()

    @Serializable
    data object Trainer: Screen()

    @Serializable
    data object Plan: Screen()
}