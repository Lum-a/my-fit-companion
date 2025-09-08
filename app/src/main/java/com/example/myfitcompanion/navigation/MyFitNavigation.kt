package com.example.myfitcompanion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myfitcompanion.screen.Screen
import com.example.myfitcompanion.screen.classes.ClassesScreen
import com.example.myfitcompanion.screen.splash.SplashScreen
import com.example.myfitcompanion.screen.home.HomeScreen
import com.example.myfitcompanion.screen.login.LoginScreen
import com.example.myfitcompanion.screen.plan.PlanScreen
import com.example.myfitcompanion.screen.profile.ProfileScreen
import com.example.myfitcompanion.screen.signup.RegisterScreen
import com.example.myfitcompanion.screen.trainer.TrainerScreen

@Composable
fun MyFitNavigation(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
        modifier = Modifier.padding(padding)
    ) {
        composable<Screen.Splash> {
            SplashScreen(
                onNavigateToHome = { navController.navigate(Screen.Home) },
                onNavigateToLogin = { navController.navigate(Screen.Login) },
                onNavigateToRegister = { navController.navigate(Screen.Register) }
            )
        }
        composable<Screen.Login> { LoginScreen() }
        composable<Screen.Register> { RegisterScreen() }
        composable<Screen.Profile> { ProfileScreen() }
        composable<Screen.Home> {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Screen.Classes> { ClassesScreen()}
        composable<Screen.Trainer> { TrainerScreen() }
        composable<Screen.Plan> { PlanScreen() }
    }
}