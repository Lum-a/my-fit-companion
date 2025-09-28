package com.example.myfitcompanion.navigation

import android.util.Log
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
import com.example.myfitcompanion.screen.chat.ChatScreen
import com.example.myfitcompanion.screen.workout.split.exercise.YouTubePlayerScreen
import com.example.myfitcompanion.screen.workout.split.exercise.ExerciseScreen
import com.example.myfitcompanion.screen.workout.WorkoutScreen
import com.example.myfitcompanion.screen.splash.SplashScreen
import com.example.myfitcompanion.screen.home.HomeScreen
import com.example.myfitcompanion.screen.login.LoginScreen
import com.example.myfitcompanion.screen.meal.MealScreen
import com.example.myfitcompanion.screen.profile.ChangeEmailScreen
import com.example.myfitcompanion.screen.profile.ChangeEmailScreen
import com.example.myfitcompanion.screen.meal.MealViewModel
import com.example.myfitcompanion.screen.profile.ChangePasswordScreen
import com.example.myfitcompanion.screen.profile.ProfileScreen
import com.example.myfitcompanion.screen.profile.SettingsScreen
import com.example.myfitcompanion.screen.profile.SettingsScreen
import com.example.myfitcompanion.screen.signup.RegisterScreen
import com.example.myfitcompanion.screen.trainer.TrainerScreen
import com.example.myfitcompanion.screen.workout.split.SplitScreen
import com.example.myfitcompanion.utils.AuthViewModel
import androidx.compose.runtime.collectAsState

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
            )
        }

        composable<Screen.YoutubePlayer> {
            val video = it.toRoute<Screen.YoutubePlayer>()
            YouTubePlayerScreen(
                videoId = video.videoUrl,
                onBack = { navController.popBackStack() })
        }

        composable<Screen.Login> {
            LoginScreen(
                onLoginSucceed = { isAdmin ->
                    val screen = if (isAdmin) AdminScreen.Admin else Screen.Home
                    navigate(screen)
                },
                onSignUp = { navController.navigate(Screen.Register) }
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSucceed = {
                    val screen = if (isAdmin) AdminScreen.Admin else Screen.Home
                    navigate(screen)
                },
                onLogin = {navController.navigate(Screen.Login)}
            )
        }
        composable<Screen.Profile> {
            ProfileScreen(
                onProfileUpdated = { navController.navigate(Screen.Home) },
                onChangePassword = { navigate(Screen.ChangePassword) },
                onNavigateToSettings = { navigate(Screen.Settings) }
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                onChangeEmail = { navigate(Screen.ChangeEmail) },
                onChangePassword = { navigate(Screen.ChangePassword) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.ChangeEmail> {
            ChangeEmailScreen(
                onEmailChanged = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.ChangePassword> {
            ChangePasswordScreen(
                onPasswordChanged = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                onLogout = { logout() },
                onTrainersClick = { navigate(Screen.Trainer) },
                onWorkoutClick = { navigate(Screen.Workout) },
                onMealsClick = { navigate(Screen.Meal) },
                onProfileClick = { navigate(Screen.Profile) }
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
        composable<Screen.Meal> {
            val viewModel: MealViewModel = hiltViewModel()
            val meals = viewModel.meals.collectAsState()
            MealScreen(meals = meals.value)
        }
        composable<Screen.Trainer> { TrainerScreen(
            onTrainerClick = { userId, userName, peerId, peerName ->
                navigate(Screen.Chat(userId, userName, peerId, peerName))
            }
        ) }
        composable<Screen.Chat> {
            val chat = it.toRoute<Screen.Chat>()
            Log.d("MyFitNavigation", "Navigating to Chat with data: $chat")
            ChatScreen(
                userId = chat.userId,
                peerId = chat.peerId,
                userName = chat.userName,
                peerName = chat.peerName,
                onNavigateBack = { navController.popBackStack() }
            )
        }


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
        composable<AdminScreen.User> { AdminUserScreen(onBack = { navController.popBackStack() }) }
        composable<AdminScreen.Meals> { AdminMealScreen(onBack = { navController.popBackStack() }) }
        composable<AdminScreen.Workout> {
            AdminWorkoutScreen(
                onBack = { navController.popBackStack() },
                onWorkoutClick = { workoutId ->
                    navController.navigate(AdminScreen.Split(workoutId))
                }
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