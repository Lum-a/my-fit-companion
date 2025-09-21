package com.example.myfitcompanion.screen.workout.split.exercise

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myfitcompanion.screen.workout.WorkoutViewModel

/**
 * Created by Edon Idrizi on 19/Sep/2025 :)
 **/

@Composable
fun ExerciseScreen(
    splitId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadExercises(splitId)
    }

}