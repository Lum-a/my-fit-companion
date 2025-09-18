package com.example.myfitcompanion.admin.screen

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
import com.example.myfitcompanion.api.model.SessionRequest
import com.example.myfitcompanion.api.model.SessionsResponse
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSessionScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onSessionClick: (SessionsResponse) -> Unit = {}
) {
    val sessionsState by viewModel.sessions.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Sessions") },
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
                Icon(Icons.Default.Add, contentDescription = "Add Session")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = sessionsState) {
                is ResultWrapper.Initial -> {
                    Log.d("AdminSessionScreen", "Initial state, loading sessions...")
                    LaunchedEffect(Unit) { viewModel.loadSessions() }
                }
                is ResultWrapper.Loading -> {
                    Log.d("AdminSessionScreen", "Loading sessions...")
                    CircularProgressIndicator(color = myFitColors.current.gold)
                }
                is ResultWrapper.Error -> {
                    Log.d("AdminSessionScreen", "Error loading sessions: ${state.message}")
                    Text(
                        "Failed to load sessions",
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
                        items(state.data) { session ->
                            SessionCard(
                                session = session,
                                onClick = { onSessionClick(session) },
                                onEdit = { /* TODO: open edit dialog */ },
                                onDelete = { viewModel.deleteSession(session.id) }
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
                        val durationInt = duration.toIntOrNull()
                        val userIdInt = userId.toIntOrNull()
                        if (name.isNotBlank() && date.isNotBlank() && durationInt != null && userIdInt != null) {
                            viewModel.addSession(
                                SessionRequest(
                                    name = name,
                                    date = date,
                                    duration = durationInt,
                                    userId = userIdInt
                                )
                            )
                            showDialog = false
                            name = ""
                            date = ""
                            duration = ""
                            userId = ""
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
                title = { Text("Create Session") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Date (YYYY-MM-DD)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text("Duration (min)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = userId,
                            onValueChange = { userId = it },
                            label = { Text("User ID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SessionCard(
    session: SessionsResponse,
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
                Text(session.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(session.date, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Text("Duration: ${session.duration} min", color = myFitColors.current.gold, style = MaterialTheme.typography.bodySmall)
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
