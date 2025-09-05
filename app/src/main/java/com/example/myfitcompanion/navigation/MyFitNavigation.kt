package com.example.myfitcompanion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfitcompanion.screen.Screens
import com.example.myfitcompanion.screen.splash.SplashScreen
import com.example.myfitcompanion.screen.home.HomeScreen
import com.example.myfitcompanion.screen.login.LoginScreen
import com.example.myfitcompanion.screen.profile.ProfileScreen
import com.example.myfitcompanion.screen.signup.SignUpScreen

@Composable
fun MyFitNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Splash) {
        composable<Screens.Splash> {
            SplashScreen(
                onFinished = { navController.navigate(Screens.Login) }
            )
        }
        composable<Screens.Login> { LoginScreen() }
        composable<Screens.SignUp> { SignUpScreen() }
        composable<Screens.Profile> { ProfileScreen() }
        composable<Screens.Home> { HomeScreen() }
    }
}