package com.example.myfitcompanion.screen

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object Splash: Screens()

    @Serializable
    data object Login: Screens()

    @Serializable
    data object SignUp: Screens()

    @Serializable
    data object Home: Screens()

    @Serializable
    data object Profile: Screens()
}