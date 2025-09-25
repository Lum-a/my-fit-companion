package com.example.myfitcompanion.admin.screen.workout

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
import com.example.myfitcompanion.api.model.ExerciseRequest
import com.example.myfitcompanion.admin.viewmodel.AdminViewModel
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminExerciseScreen(
    splitId: Int,
    viewModel: AdminViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val exercisesState by viewModel.exercises.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingExercise by remember { mutableStateOf<ExerciseResponse?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var videoId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Exercises") },
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
                onClick = {
                    editingExercise = null
                    name = ""
                    description = ""
                    videoId = ""
                    showDialog = true
                },
                containerColor = myFitColors.current.gold
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = exercisesState) {
                is ResultWrapper.Initial -> {
                    LaunchedEffect(Unit) { viewModel.loadExercises(splitId) }
                }
                is ResultWrapper.Loading -> {
                    CircularProgressIndicator(color = myFitColors.current.gold)
                }
                is ResultWrapper.Error -> {
                    Text(
                        "Failed to load exercises",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ResultWrapper.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { exercise ->
                            ExerciseCard(
                                exercise = exercise,
                                onEdit = {
                                    editingExercise = exercise
                                    name = exercise.name
                                    description = exercise.description ?: ""
                                    videoId = exercise.videoId
                                    showDialog = true
                                },
                                onDelete = {
                                    viewModel.deleteExercise(exercise.id)
                                }
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
                        if (name.isNotBlank() && videoId.isNotBlank()) {
                            val req = ExerciseRequest(
                                name = name,
                                description = description,
                                videoId = videoId,
                            )

                            editingExercise?.let {
                                viewModel.updateExercise(it.id, req)
                            } ?: viewModel.addExercise(splitId = splitId, req)

                            showDialog = false
                            editingExercise = null
                            name = ""
                            description = ""
                            videoId = ""
                        }
                    }) {
                        Text(if (editingExercise != null) "Update" else "Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text(if (editingExercise != null) "Edit Exercise" else "Create Exercise") },
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
                            value = videoId,
                            onValueChange = { videoId = it },
                            label = { Text("Video ID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(exercise.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(exercise.description ?: "", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
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
