package com.example.myfitcompanion.screen.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.components.VerticalSpace
import com.example.myfitcompanion.model.register.RegisterRequest
import com.example.myfitcompanion.utils.ResultWrapper

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.registerState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = bodyFat,
            onValueChange = { bodyFat = it },
            label = { Text("Body Fat (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            label = { Text("Goal") }
        )

        VerticalSpace(modifier, 16.dp)

        when (state) {
            is ResultWrapper.Initial -> {}
            is ResultWrapper.Loading -> CircularProgressIndicator()
            is ResultWrapper.Error -> {
                Text(
                    text = "Error: ${(state as ResultWrapper.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            is ResultWrapper.Success -> {
                Text(
                    text = "Registration Successful!",
                    color = MaterialTheme.colorScheme.primary
                )
            }


        }

        Button(
            onClick = {
                viewModel.register(
                    RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    height = height.toFloatOrNull(),
                    weight = weight.toFloatOrNull(),
                    bodyFat = bodyFat.toFloatOrNull(),
                    goal = goal.ifBlank { null }
                ))
            },
            modifier = modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}