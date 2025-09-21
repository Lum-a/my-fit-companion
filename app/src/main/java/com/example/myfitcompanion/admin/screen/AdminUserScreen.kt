package com.example.myfitcompanion.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myfitcompanion.admin.viewmodel.AdminViewModel
import com.example.myfitcompanion.api.model.CreateUserRequest
import com.example.myfitcompanion.api.model.UpdateUserRequest
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val usersState by viewModel.users.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    var userToEdit by remember { mutableStateOf<UserResponse?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Users") },
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
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = usersState) {
                is ResultWrapper.Initial -> {
                    LaunchedEffect(Unit) { viewModel.loadUsers() }
                }
                is ResultWrapper.Loading -> {
                    CircularProgressIndicator(color = myFitColors.current.gold)
                }
                is ResultWrapper.Error -> {
                    Text(
                        "Failed to load users",
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
                        items(state.data) { user ->
                            UserCard(
                                user = user,
                                onEdit = { userToEdit = user},
                                onDelete = { viewModel.deleteUser(user.id) }
                            )
                        }
                    }
                }
            }
        }
        if (showDialog) {
            UserDialog(
                isUpdate = false,
                onDismiss = { showDialog = false },
                onCreate = { createUserRequest ->
                    viewModel.addUser(createUserRequest)
                    showDialog = false
                }
            )
        }
        userToEdit?.let { user ->
            UserDialog(
                isUpdate = true,
                initialFirstName = user.firstName ?: "",
                initialLastName = user.lastName ?: "",
                initialEmail = user.email,
                initialRole = user.role,
                onDismiss = { userToEdit = null },
                onUpdate = { request ->
                    viewModel.updateUser(user.id, request)
                    userToEdit = null
                }
            )
        }
    }
}

@Composable
fun UserCard(
    user: UserResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = myFitColors.current.cardsGrey
        ),
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
                Text(user.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(user.email, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Text(user.role, color = myFitColors.current.gold, style = MaterialTheme.typography.bodySmall)
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

@Composable
fun UserDialog(
    isUpdate: Boolean,
    initialFirstName: String = "",
    initialLastName: String = "",
    initialEmail: String = "",
    initialRole: String = "",
    onDismiss: () -> Unit,
    onCreate: (CreateUserRequest) -> Unit = {},
    onUpdate: (UpdateUserRequest) -> Unit = {}
) {
    var firstName by remember { mutableStateOf(initialFirstName) }
    var lastName by remember { mutableStateOf(initialLastName) }
    var email by remember { mutableStateOf(initialEmail) }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(initialRole) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && role.isNotBlank()) {
                    if (isUpdate) {
                        onUpdate(
                            UpdateUserRequest(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                role = role.uppercase()
                            )
                        )
                    } else {
                        onCreate(
                            CreateUserRequest(
                                email = email,
                                password = password,
                                firstName = firstName,
                                lastName = lastName,
                                role = role.uppercase()
                            )
                        )
                    }
                }
            }) {
                Text(if (isUpdate) "Update" else "Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(if (isUpdate) "Update User" else "Create User") },
        text = {
            Column {
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
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (!isUpdate) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

