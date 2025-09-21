package com.example.myfitcompanion.screen

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
    data object Home: Screen()

    @Serializable
    data object Profile: Screen()

    @Serializable
    data object Workout: Screen()

    @Serializable
    data object Trainer: Screen()

    @Serializable
    data object Meal: Screen()

    @Serializable
    data object Exercise: Screen()
}

@Serializable
sealed class AdminScreen {

    @Serializable
    data object Admin: Screen()

    @Serializable
    data object User: Screen()

    @Serializable
    data object Workout: Screen()

    @Serializable
    data object Meals: Screen()

    @Serializable
    data object Exercise: Screen()

    @Serializable
    data object Trainer: Screen()

}