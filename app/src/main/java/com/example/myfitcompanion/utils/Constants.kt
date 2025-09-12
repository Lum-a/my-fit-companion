package com.example.myfitcompanion.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.myfitcompanion.screen.BottomNavItem
import com.example.myfitcompanion.screen.Screen

object Constants {
    private const val HOME_LABEL = "Home"
    private const val CLASS_LABEL = "Classes"
    private const val PLANS_LABEL = "Plans"
    private const val PROFILE_LABEL = "Profile"

    val bottomNavItems = listOf(
        BottomNavItem(
            label = HOME_LABEL,
            icon = Icons.Filled.Home,
            route = Screen.Home
        ),

        BottomNavItem(
            label = CLASS_LABEL,
            icon = Icons.Filled.Menu,
            route = Screen.Classes
        ),

        BottomNavItem(
            label = PLANS_LABEL,
            icon = Icons.Filled.ShoppingCart,
            route = Screen.Plan
        ),
        BottomNavItem(
            label = PROFILE_LABEL,
            icon = Icons.Filled.Person,
            route = Screen.Profile
        )
    )
}