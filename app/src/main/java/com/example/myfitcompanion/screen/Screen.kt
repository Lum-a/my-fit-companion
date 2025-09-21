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
    data class Split(val workoutId: Int): Screen()

    @Serializable
    data class Exercise(val splitId: Int): Screen()

    @Serializable
    data object Trainer: Screen()

    @Serializable
    data object ChangePassword: Screen()

    data object Meal: Screen()

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
    data class Split(val workoutId: Int): Screen()

    @Serializable
    data class Exercise(val splitId: Int): Screen()

    @Serializable
    data object Meals: Screen()

    @Serializable
    data object Trainer: Screen()

}