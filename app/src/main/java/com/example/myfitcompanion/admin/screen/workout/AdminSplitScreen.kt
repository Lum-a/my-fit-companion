package com.example.myfitcompanion.admin.screen.workout

import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myfitcompanion.R
import com.example.myfitcompanion.admin.viewmodel.AdminViewModel
import com.example.myfitcompanion.api.model.SplitRequest
import com.example.myfitcompanion.api.model.SplitResponse
import com.example.myfitcompanion.components.ImagePickerHandler
import com.example.myfitcompanion.model.WorkoutType
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSplitScreen(
    workoutId: Int,
    onSplitClick: (splitId: Int) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: AdminViewModel = hiltViewModel()
) {
    val splitsState by viewModel.splits.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var editingSplit by remember { mutableStateOf<SplitResponse?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var uri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(workoutId) {
        viewModel.loadSplits(workoutId)
    }

    val workoutName = WorkoutType.entries
        .find { it.id == workoutId }?.displayName ?: "Unknown Workout"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage $workoutName Splits") },
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
                    editingSplit = null
                    name = ""
                    description = ""
                    showDialog = true
                },
                containerColor = myFitColors.current.gold
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Split")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = splitsState) {
                is ResultWrapper.Initial -> {}
                is ResultWrapper.Loading -> {
                    CircularProgressIndicator(color = myFitColors.current.gold)
                }
                is ResultWrapper.Error -> {
                    Text(
                        "Failed to load splits",
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
                        items(state.data) { split ->
                            SplitCard(
                                split = split,
                                onClick = { onSplitClick(split.splitId) },
                                onEdit = {
                                    editingSplit = split
                                    name = split.name
                                    description = split.description
                                    showDialog = true
                                },
                                onDelete = {
                                    viewModel.deleteSplit(split.splitId)
                                    // Reload splits after delete
                                    viewModel.loadSplits(workoutId)
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
                        if (name.isNotBlank()) {
                            val req = SplitRequest(
                                name = name,
                                description = description,
                            )
                            editingSplit?.let {
                                viewModel.updateSplit(it.splitId, req)
                            } ?: viewModel.addSplit(workoutId,req, uri)


                            showDialog = false
                            editingSplit = null
                            name = ""
                            description = ""
                            uri = null
                        }
                    }) {
                        Text(if (editingSplit != null) "Update" else "Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text(if (editingSplit != null) "Edit Split" else "Create Split") },
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

                        ImagePickerHandler(onImageSelected = { uri = it }) { onClick ->
                            Row {
                                IconButton(
                                    onClick = onClick,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color.Transparent),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "Change Photo",
                                        tint = myFitColors.current.gold
                                    )
                                }
                                if (uri != null) {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = "Profile Photo",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SplitCard(
    split: SplitResponse,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).clickable { onClick() }) {
                Text(split.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(split.description, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
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