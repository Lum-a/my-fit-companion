package com.example.myfitcompanion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.myfitcompanion.api.model.UserResponse
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

    fun navigate(route: Any) {
        return navController.navigate(route)
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
        modifier = Modifier.padding(padding)
    ) {
        composable<Screen.Splash> {
            SplashScreen(
                onNavigateToHome = { user -> navigate(Screen.Home(user)) },
                onNavigateToLogin = { navigate(Screen.Login) },
                onNavigateToRegister = { navigate(Screen.Register) }
            )
        }
        composable<Screen.Login> {
            LoginScreen(
                onLoginSucceed = { user -> navigate(Screen.Home(user)) }
            )
        }
        composable<Screen.Register> {
            RegisterScreen(onRegisterSucceed = { user ->
                navigate(Screen.Home(user))
            })
        }
        composable<Screen.Profile> { ProfileScreen() }
        composable<Screen.Home>(
            typeMap = mapOf(typeMapOf<UserResponse>())
        ) {
            val home: Screen.Home = it.toRoute()
            HomeScreen(
                userResponse = home.userResponse,
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