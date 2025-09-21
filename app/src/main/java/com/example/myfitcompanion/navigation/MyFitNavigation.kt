package com.example.myfitcompanion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.myfitcompanion.admin.screen.workout.AdminExerciseScreen
import com.example.myfitcompanion.admin.screen.AdminMealScreen
import com.example.myfitcompanion.admin.screen.AdminScreen
import com.example.myfitcompanion.admin.screen.workout.AdminWorkoutScreen
import com.example.myfitcompanion.admin.screen.AdminTrainerScreen
import com.example.myfitcompanion.admin.screen.AdminUserScreen
import com.example.myfitcompanion.admin.screen.workout.AdminSplitScreen
import com.example.myfitcompanion.screen.AdminScreen
import com.example.myfitcompanion.screen.Screen
import com.example.myfitcompanion.screen.workout.split.exercise.ExerciseScreen
import com.example.myfitcompanion.screen.workout.WorkoutScreen
import com.example.myfitcompanion.screen.splash.SplashScreen
import com.example.myfitcompanion.screen.home.HomeScreen
import com.example.myfitcompanion.screen.login.LoginScreen
import com.example.myfitcompanion.screen.meal.MealScreen
import com.example.myfitcompanion.screen.profile.ProfileScreen
import com.example.myfitcompanion.screen.signup.RegisterScreen
import com.example.myfitcompanion.screen.trainer.TrainerScreen
import com.example.myfitcompanion.screen.workout.split.SplitScreen
import com.example.myfitcompanion.utils.AuthViewModel

@Composable
fun MyFitNavigation(navController: NavHostController, padding: PaddingValues, isAdmin: Boolean) {

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
            LoginScreen(
                onLoginSucceed = { isAdmin ->
                    val screen = if (isAdmin) AdminScreen.Admin else Screen.Home
                    navigate(screen)
                }
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSucceed = {
                    val screen = if (isAdmin) AdminScreen.Admin else Screen.Home
                    navigate(screen)
                }
            )
        }
        composable<Screen.Profile> { ProfileScreen() }
        composable<Screen.Home> {
            HomeScreen(
                onLogout = { logout() },
                onTrainersClick = { navigate(Screen.Trainer) },
                onWorkoutClick = { navigate(Screen.Workout) },
                onMealsClick = { navigate(Screen.Meal) },
            )
        }
        composable<Screen.Workout> {
            WorkoutScreen(
                onWorkoutClick = { workoutId ->
                    navigate(Screen.Split(workoutId))
                }
            )
        }
        composable<Screen.Split> {
            val workout = it.toRoute<Screen.Split>()
            SplitScreen(
                workoutId = workout.workoutId,
                onSplitClick = { splitId ->
                    navigate(Screen.Exercise(splitId))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Exercise> {
            val split = it.toRoute<Screen.Exercise>()
            ExerciseScreen(
                splitId = split.splitId,
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Meal> { MealScreen() }
        composable<Screen.Trainer> { TrainerScreen(onTrainerClick = { navigate(Screen.Profile) }) }

        //Admin screens
        composable<AdminScreen.Admin> {
            AdminScreen(
                onNavigateToUsers = { navigate(AdminScreen.User) },
                onNavigateToMeals = { navigate(AdminScreen.Meals) },
                onNavigateToWorkouts = { navigate(AdminScreen.Workout) },
                onNavigateToTrainers = { navigate(AdminScreen.Trainer) },
                onLogout = { logout() }
            )
        }

        //admin screens
        composable<AdminScreen.User> { AdminUserScreen(onBack = { navController.popBackStack() }) }
        composable<AdminScreen.Meals> { AdminMealScreen(onBack = { navController.popBackStack() }) }
        composable<AdminScreen.Workout> {
            AdminWorkoutScreen(
                onBack = { navController.popBackStack() },
                onWorkoutClick = { workoutId ->
                    navController.navigate(AdminScreen.Split(workoutId)) }
            )
        }
        composable<AdminScreen.Split> {
            val workout = it.toRoute<AdminScreen.Split>()
            AdminSplitScreen(
                workoutId = workout.workoutId,
                onSplitClick = { splitId ->
                    navController.navigate(AdminScreen.Exercise(splitId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<AdminScreen.Exercise> {
            val split = it.toRoute<AdminScreen.Exercise>()
            AdminExerciseScreen(
                splitId = split.splitId,
                onBack = { navController.popBackStack() }
            )
        }
        composable<AdminScreen.Trainer> {
            AdminTrainerScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}