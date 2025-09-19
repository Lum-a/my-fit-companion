package com.example.myfitcompanion.admin.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.admin.viewmodel.AdminViewModel
import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.api.model.TrainerRequest
import com.example.myfitcompanion.api.model.UpdateTrainerRequest
import com.example.myfitcompanion.utils.ResultWrapper

/**
 * Created by Edon Idrizi on 19/Sep/2025 :)
 **/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTrainerScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: AdminViewModel = hiltViewModel()
) {
    val trainersState by viewModel.trainers.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }
    var editingTrainer by remember { mutableStateOf<TrainerResponse?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadTrainers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Trainers") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Trainer")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = trainersState) {
                is ResultWrapper.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ResultWrapper.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.data) { trainer ->
                            TrainerItem(
                                trainer = trainer,
                                onEdit = { editingTrainer = it },
                                onDelete = { viewModel.deleteTrainer(it.trainerId) }
                            )
                        }
                    }
                }
                is ResultWrapper.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is ResultWrapper.Initial -> {
                    // Show nothing or loading state
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateTrainerDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { trainerRequest ->
                viewModel.addTrainer(trainerRequest)
                showCreateDialog = false
            }
        )
    }

    editingTrainer?.let { trainer ->
        EditTrainerDialog(
            trainer = trainer,
            onDismiss = { editingTrainer = null },
            onSave = { updateRequest ->
                viewModel.updateTrainer(trainer.trainerId, updateRequest)
                editingTrainer = null
            }
        )
    }
}

@Composable
private fun TrainerItem(
    trainer: TrainerResponse,
    onEdit: (TrainerResponse) -> Unit,
    onDelete: (TrainerResponse) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${trainer.firstName} ${trainer.lastName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Email: ${trainer.email}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    trainer.specialization?.let {
                        Text(
                            text = "Specialization: $it",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    trainer.contactInfo?.let {
                        Text(
                            text = "Contact: $it",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Row {
                    IconButton(onClick = { onEdit(trainer) }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { onDelete(trainer) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateTrainerDialog(
    onDismiss: () -> Unit,
    onSave: (TrainerRequest) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Trainer")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    isError = password != confirmPassword && confirmPassword.isNotEmpty()
                )
                if (password != confirmPassword && confirmPassword.isNotEmpty()) {
                    Text(
                        text = "Passwords do not match",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                OutlinedTextField(
                    value = specialization,
                    onValueChange = { specialization = it },
                    label = { Text("Specialization") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contactInfo,
                    onValueChange = { contactInfo = it },
                    label = { Text("Contact Info") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (firstName.isNotBlank() && lastName.isNotBlank() &&
                        email.isNotBlank() && password.isNotBlank() &&
                        password == confirmPassword) {
                        onSave(
                            TrainerRequest(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                password = password,
                                specialization = specialization.takeIf { it.isNotBlank() },
                                contactInfo = contactInfo.takeIf { it.isNotBlank() }
                            )
                        )
                    }
                },
                enabled = firstName.isNotBlank() && lastName.isNotBlank() &&
                         email.isNotBlank() && password.isNotBlank() &&
                         password == confirmPassword
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun EditTrainerDialog(
    trainer: TrainerResponse,
    onDismiss: () -> Unit,
    onSave: (UpdateTrainerRequest) -> Unit
) {
    var firstName by remember { mutableStateOf(trainer.firstName) }
    var lastName by remember { mutableStateOf(trainer.lastName) }
    var email by remember { mutableStateOf(trainer.email) }
    var specialization by remember { mutableStateOf(trainer.specialization ?: "") }
    var contactInfo by remember { mutableStateOf(trainer.contactInfo ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Trainer")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = specialization,
                    onValueChange = { specialization = it },
                    label = { Text("Specialization") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contactInfo,
                    onValueChange = { contactInfo = it },
                    label = { Text("Contact Info") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()) {
                        onSave(
                            UpdateTrainerRequest(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                specialization = specialization.takeIf { it.isNotBlank() },
                                contactInfo = contactInfo.takeIf { it.isNotBlank() }
                            )
                        )
                    }
                },
                enabled = firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}