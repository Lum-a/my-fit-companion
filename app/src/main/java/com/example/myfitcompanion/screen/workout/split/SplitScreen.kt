package com.example.myfitcompanion.screen.workout.split

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myfitcompanion.screen.workout.WorkoutViewModel

@Composable
fun SplitScreen(
    workoutId: Int,
    onSplitClick: (splitId: Int) -> Unit,
    onBack: () -> Unit = {},
    viewModel: WorkoutViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadSplits(workoutId)
    }
}