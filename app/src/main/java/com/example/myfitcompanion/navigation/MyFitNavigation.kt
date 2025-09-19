package com.example.myfitcompanion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myfitcompanion.admin.screen.AdminExerciseScreen
import com.example.myfitcompanion.admin.screen.AdminMealScreen
import com.example.myfitcompanion.admin.screen.AdminScreen
import com.example.myfitcompanion.admin.screen.AdminSessionScreen
import com.example.myfitcompanion.admin.screen.AdminTrainerScreen
import com.example.myfitcompanion.admin.screen.AdminUserScreen
import com.example.myfitcompanion.screen.AdminScreen
import com.example.myfitcompanion.screen.Screen
import com.example.myfitcompanion.screen.exercise.ExerciseScreen
import com.example.myfitcompanion.screen.session.SessionScreen
import com.example.myfitcompanion.screen.splash.SplashScreen
import com.example.myfitcompanion.screen.home.HomeScreen
import com.example.myfitcompanion.screen.login.LoginScreen
import com.example.myfitcompanion.screen.meal.MealScreen
import com.example.myfitcompanion.screen.profile.ProfileScreen
import com.example.myfitcompanion.screen.signup.RegisterScreen
import com.example.myfitcompanion.screen.trainer.TrainerScreen
import com.example.myfitcompanion.utils.AuthViewModel

@Composable
fun MyFitNavigation(navController: NavHostController, padding: PaddingValues, isAdmin: Boolean = false) {

    fun navigate(route: Any) {
        return navController.navigate(route)
    }

    val authViewModel = hiltViewModel<AuthViewModel>()

    fun logout() {
        authViewModel.logout()
        navController.navigate(Screen.Login) {
            popUpTo(0) {
                inclusive = true
            }
        }
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
                onLogout = { logout() },
                onTrainersClick = { navigate(Screen.Trainer) },
                onSessionsClick = { navigate(Screen.Session) },
                onMealsClick = { navigate(Screen.Meal) },
            )
        }
        composable<Screen.Session> { SessionScreen(onSessionClick = { navigate(Screen.Exercise) }) }
        composable<Screen.Exercise> { ExerciseScreen() }
        composable<Screen.Meal> { MealScreen() }
        composable<Screen.Trainer> { TrainerScreen(onTrainerClick = { navigate(Screen.Profile) }) }

        //Admin screens
        composable<AdminScreen.Admin> {
            AdminScreen(
                onNavigateToUsers = { navigate(AdminScreen.User) },
                onNavigateToMeals = { navigate(AdminScreen.Meals) },
                onNavigateToSessions = { navigate(AdminScreen.Session) },
                onNavigateToTrainers = { navigate(AdminScreen.Trainer) },
                onLogout = { logout() }
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
        composable<AdminScreen.Trainer> { AdminTrainerScreen(onBack = { navController.popBackStack() }) }

    }
}