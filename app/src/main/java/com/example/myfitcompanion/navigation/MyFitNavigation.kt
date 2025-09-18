package com.example.myfitcompanion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myfitcompanion.admin.screen.AdminExerciseScreen
import com.example.myfitcompanion.admin.screen.AdminMealScreen
import com.example.myfitcompanion.admin.screen.AdminScreen
import com.example.myfitcompanion.admin.screen.AdminSessionScreen
import com.example.myfitcompanion.admin.screen.AdminUserScreen
import com.example.myfitcompanion.screen.AdminScreen
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
fun MyFitNavigation(navController: NavHostController, padding: PaddingValues, isAdmin: Boolean = false) {

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
                onNavigateToHome = {
                    val screen = if (isAdmin) AdminScreen.Admin else Screen.Home
                    navigate(screen)
                },
                onNavigateToLogin = { navigate(Screen.Login) },
                onNavigateToRegister = { navigate(Screen.Register) }
            )
        }
        composable<Screen.Login> {
            val screen = if(isAdmin) AdminScreen.Admin else Screen.Home
            LoginScreen(
                onLoginSucceed = { navigate(screen) }
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSucceed = { navigate(Screen.Home) }
            )
        }
        composable<Screen.Profile> { ProfileScreen() }
        composable<Screen.Home> {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                onTrainersClick = { navigate(Screen.Trainer) },
                onClassesClick = { navigate(Screen.Classes) },
                onPlansClick = { navigate(Screen.Plan) },
            )
        }
        composable<Screen.Classes> { ClassesScreen() }
        composable<Screen.Trainer> { TrainerScreen(
            onTrainerClick = { navigate(Screen.Profile) }
        ) }
        composable<Screen.Plan> { PlanScreen() }

        //Admin screens
        composable<AdminScreen.Admin> {
            AdminScreen(
                onNavigateToUsers = {
                    navController.navigate(AdminScreen.User)
                },
                onNavigateToMeals = {
                    navController.navigate(AdminScreen.Meals)
                },
                onNavigateToSessions = {
                    navController.navigate(AdminScreen.Session)
                }
            )
        }

        //admin screens
        composable<AdminScreen.User> { AdminUserScreen(onBack = { navController.popBackStack() }) }
        composable<AdminScreen.Meals> { AdminMealScreen(onBack = { navController.popBackStack() }) }
        composable<AdminScreen.Session> {
            AdminSessionScreen(
                onBack = { navController.popBackStack() },
                onSessionClick = { navController.navigate(AdminScreen.Exercise) }
            )
        }
        composable<AdminScreen.Exercise> { AdminExerciseScreen(onBack = { navController.popBackStack() }) }

    }
}