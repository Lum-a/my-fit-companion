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
import com.example.myfitcompanion.utils.isValidEmail

@Composable
fun ChangeEmailScreen(
    onEmailChanged: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    var currentEmail by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }

    // Initialize current email when user loads
    LaunchedEffect(user) {
        currentEmail = user?.email ?: ""
    }

    // Handle navigation after successful email change
    LaunchedEffect(success) {
        if (success) {
            kotlinx.coroutines.delay(1000)
            onEmailChanged()
        }
    }

    fun resetErrors() {
        errorText = ""
        success = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with back button
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

        Spacer(modifier = Modifier.height(24.dp))

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
            onValueChange = { newEmail = it; resetErrors() },
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

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmEmail,
            onValueChange = { confirmEmail = it; resetErrors() },
            label = { Text("Confirm New Email", color = Color.Gray) },
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

        if (errorText.isNotBlank()) {
            Text(
                errorText,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }
        if (success) {
            Text(
                "Email changed successfully!",
                color = myFitColors.current.gold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
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
                    resetErrors()
                    if (!isValidEmail(newEmail)) {
                        errorText = "Please enter a valid email address"
                        return@Button
                    }
                    if (newEmail != confirmEmail) {
                        errorText = "Email addresses do not match"
                        return@Button
                    }
                    if (newEmail == currentEmail) {
                        errorText = "New email must be different from current email"
                        return@Button
                    }

                    isLoading = true
                    // TODO: Call update email API here
                    // For now, just show success
                    isLoading = false
                    success = true
                },
                enabled = newEmail.isNotBlank() && confirmEmail.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = myFitColors.current.gold,
                    contentColor = Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                if (isLoading) {
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
