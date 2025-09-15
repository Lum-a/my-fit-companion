package com.example.myfitcompanion.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val darkBackground = Color(0xFF1F1E1E)
val cardGreyColor = Color(0xFF1C1C1C)
val yellowColor = Color(0xFFFFEB3B)
val orangeColor = Color(0xFFFFA726)
val lightOrangeColor = Color(0xFFFFCC80)
val goldColor = Color(0xFFFFD700)

@Immutable
data class MyFitColors(
    val background: Color = darkBackground,
    val orange: Color = orangeColor,
    val yellow: Color = yellowColor,
    val cardsGrey: Color = cardGreyColor,
    val lightOrange: Color =lightOrangeColor,
    val gold: Color = goldColor,
)

val LightCustomColorsPalette = MyFitColors()

val DarkCustomColorsPalette = MyFitColors()

val myFitColors = staticCompositionLocalOf { MyFitColors() }