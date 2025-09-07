package com.example.myfitcompanion.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import com.example.myfitcompanion.screen.BottomNavItem
import com.example.myfitcompanion.screen.Screen

object Constants {

    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = Screen.Home
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            route = Screen.Profile
        ),
        BottomNavItem(
            label = "Search",
            icon = Icons.Filled.Search,
            route = Screen.Classes
        )
    )
}