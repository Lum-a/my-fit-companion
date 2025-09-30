package com.example.myfitcompanion.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.R
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidEmail

@Composable
fun ChangeEmailScreen(
    onEmailChanged: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val changeEmailState by viewModel.changeEmailState.collectAsStateWithLifecycle()

    var newEmail by remember { mutableStateOf("") }

    // Initialize current email when user loads
    val currentEmail = user?.email ?: ""

    // Handle navigation after successful email change
    LaunchedEffect(changeEmailState) {
        if (changeEmailState is ResultWrapper.Success) {
            kotlinx.coroutines.delay(1000)
            onEmailChanged()
        }
    }

    // Reset state when screen is first opened
    LaunchedEffect(Unit) {
        viewModel.resetChangeEmailState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp)
    ) {
        // Header with back button - moved to top
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = myFitColors.current.gold
                )
            }
            Text(
                "Change Email",
                style = MaterialTheme.typography.headlineMedium.copy(color = myFitColors.current.gold)
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
        }

        // Center the form vertically in remaining space
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = currentEmail,
                onValueChange = { /* Read-only */ },
                label = { Text("Current Email", color = Color.Gray) },
                singleLine = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newEmail,
                onValueChange = {
                    newEmail = it
                    viewModel.resetChangeEmailState()
                },
                label = { Text("New Email", color = Color.Gray) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = myFitColors.current.gold,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = myFitColors.current.gold,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            when (val state = changeEmailState) {
                is ResultWrapper.Error -> {
                    Text(
                        state.message ?: "Unknown error occurred",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                    )
                }
                is ResultWrapper.Success -> {
                    Text(
                        state.data,
                        color = myFitColors.current.gold,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (!isValidEmail(newEmail)) {
                            return@Button
                        }
                        if (newEmail == currentEmail) {
                            return@Button
                        }
                        viewModel.changeEmail(newEmail)
                    },
                    enabled = newEmail.isNotBlank() && changeEmailState !is ResultWrapper.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myFitColors.current.gold,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    if (changeEmailState is ResultWrapper.Loading) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Change Email")
                    }
                }
            }
        }
    }
}
