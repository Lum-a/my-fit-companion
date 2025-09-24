package com.example.myfitcompanion.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.R
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidPassword

@Composable
fun ChangePasswordScreen(
    onPasswordChanged: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val changePasswordState by viewModel.changePasswordState.collectAsStateWithLifecycle()

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(changePasswordState) {
        if (changePasswordState is ResultWrapper.Success) {
            kotlinx.coroutines.delay(1000)
            onPasswordChanged()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetChangePasswordState()
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
                "Change Password",
                style = MaterialTheme.typography.headlineMedium.copy(color = myFitColors.current.gold)
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Center the form vertically in remaining space
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = oldPassword,
                onValueChange = {
                    oldPassword = it
                    viewModel.resetChangePasswordState()
                },
                label = { Text("Old Password", color = Color.Gray) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
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
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    viewModel.resetChangePasswordState()
                },
                label = { Text("New Password", color = Color.Gray) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
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
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    viewModel.resetChangePasswordState()
                },
                label = { Text("Confirm Password", color = Color.Gray) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = myFitColors.current.gold,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = myFitColors.current.gold,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            when (val state = changePasswordState) {
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White),
                    modifier = Modifier.weight(1f)
                ) { Text("Back") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        if (!isValidPassword(newPassword)) {
                            return@Button
                        }
                        if (newPassword != confirmPassword) {
                            return@Button
                        }
                        viewModel.changePassword(oldPassword, newPassword)
                    },
                    enabled = oldPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank() && changePasswordState !is ResultWrapper.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = myFitColors.current.gold, contentColor = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    if (changePasswordState is ResultWrapper.Loading) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Change Password")
                    }
                }
            }
        }
    }
}
