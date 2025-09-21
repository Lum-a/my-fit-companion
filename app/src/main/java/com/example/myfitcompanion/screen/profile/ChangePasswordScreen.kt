package com.example.myfitcompanion.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.isValidPassword

@Composable
fun ChangePasswordScreen(
    onPasswordChanged: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }

    fun resetErrors() { errorText = ""; success = false }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Change Password", style = MaterialTheme.typography.headlineMedium.copy(color = myFitColors.current.gold))
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = oldPassword,
            onValueChange = { oldPassword = it; resetErrors() },
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
            onValueChange = { newPassword = it; resetErrors() },
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
            onValueChange = { confirmPassword = it; resetErrors() },
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
        if (errorText.isNotBlank()) {
            Text(errorText, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))
        }
        if (success) {
            Text("Password changed successfully!", color = myFitColors.current.gold, style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))
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
                    resetErrors()
//                    if (oldPassword != (user?.password ?: "")) {
//                        errorText = "Old password is incorrect"
//                        return@Button
//                    }
                    if (!isValidPassword(newPassword)) {
                        errorText = "Password must be at least 8 characters, include uppercase, lowercase, and a number"
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        errorText = "Passwords do not match"
                        return@Button
                    }
                    isLoading = true
                    // Call update API
//                    viewModel.updateUserInfo(
//                        UpdateProfileRequest(
//                            password = newPassword
//                        )
//                    )
                    isLoading = false
                    success = true
                    onPasswordChanged()
                },
                enabled = oldPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = myFitColors.current.gold, contentColor = Color.Black),
                modifier = Modifier.weight(1f)
            ) { Text("Change Password") }
        }
    }
}

