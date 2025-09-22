package com.example.myfitcompanion.admin.screen.workout

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myfitcompanion.admin.viewmodel.AdminViewModel
import com.example.myfitcompanion.api.model.WorkoutRequest
import com.example.myfitcompanion.api.model.WorkoutResponse
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.launch

private const val TAG = "AdminWorkoutScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWorkoutScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onWorkoutClick: (workoutId: Int) -> Unit = {}
) {
    val workoutState by viewModel.workouts.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Workouts") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = myFitColors.current.background,
                    titleContentColor = myFitColors.current.gold
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = myFitColors.current.gold
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Workout")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = workoutState) {
                is ResultWrapper.Initial -> {
                    Log.d(TAG, "Initial state, loading workouts...")
                    LaunchedEffect(Unit) { viewModel.loadWorkouts() }
                }
                is ResultWrapper.Loading -> {
                    Log.d(TAG, "Loading workouts...")
                    CircularProgressIndicator(color = myFitColors.current.gold)
                }
                is ResultWrapper.Error -> {
                    Log.d(TAG, "Error loading workouts: ${state.message}")
                    Text(
                        "Failed to load workouts",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ResultWrapper.Success -> {
                    if(state.data.isEmpty()) {
                        Text(
                            "No workouts available. Click the + button to add a new workout.",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                        return@Box
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { workout ->
                            WorkoutCard(
                                workout = workout,
                                onClick = { onWorkoutClick(workout.id) },
                                onEdit = { /* TODO: open edit dialog */ },
                                onDelete = { viewModel.deleteWorkout(workout.id) }
                            )
                        }
                    }
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            val userId = viewModel.getUserId()
                            if (name.isNotBlank() && userId != null) {
                                viewModel.addWorkout(
                                    WorkoutRequest(
                                        name = name,
                                        description = description,
                                        imageUrl = imageUrl
                                    )
                                )
                            }
                            showDialog = false
                            name = ""
                            description = ""
                            imageUrl = ""
                        }
                    }) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Create Workout") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            label = { Text("Image URL") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun WorkoutCard(
    workout: WorkoutResponse,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(workout.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(workout.description ?: "", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = myFitColors.current.yellow)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}
