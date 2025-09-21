package com.example.myfitcompanion.admin.screen.workout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myfitcompanion.admin.viewmodel.AdminViewModel

@Composable
fun AdminSplitScreen(
    workoutId: Int,
    onSplitClick: (splitId: Int) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: AdminViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadSplits(workoutId)
    }
}