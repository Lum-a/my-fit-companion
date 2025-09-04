package com.example.myfitcompanion.screen.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel = hiltViewModel()) {
    Text(text = "Login Screen")
}