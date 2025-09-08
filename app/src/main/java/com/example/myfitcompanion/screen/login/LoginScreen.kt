package com.example.myfitcompanion.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.components.GradientButton
import com.example.myfitcompanion.components.VerticalSpace
import com.example.myfitcompanion.utils.ResultWrapper

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val isEmailValid by viewModel.isEmailValid.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") },
            isError = !isEmailValid,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (!isEmailValid) Color.Red else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = if (!isEmailValid) Color.Red else MaterialTheme.colorScheme.outline
            )
        )
        if (!isEmailValid && email.isNotEmpty()) {
            Text(
                text = "Email is invalid",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, top = 2.dp)
            )
        }

        VerticalSpace(modifier, 8.dp)

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        VerticalSpace(modifier, 16.dp)

        Button(
            onClick = { viewModel.login(password = password) },
            enabled = isEmailValid && email.isNotEmpty() && password.isNotEmpty()
        ) {
            Text("Login")
        }

        VerticalSpace(modifier, 16.dp)

        when (loginState) {
            is ResultWrapper.Initial -> {}
            is ResultWrapper.Loading -> CircularProgressIndicator()
            is ResultWrapper.Success -> Text("Login Successful!")
            is ResultWrapper.Error -> Text("Error: ${(loginState as ResultWrapper.Error).message}")
        }
    }
}

@Preview
@Composable
fun LoginScreenTest(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back 👋",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFFFA726),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Gray) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFFFFA726),
                focusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Gradient Login button
        GradientButton(
            text = "Login",
            onClick = { onLoginClick(email, password) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text(
                text = "Don’t have an account? Register",
                color = Color(0xFFFFCC80)
            )
        }
    }
}
