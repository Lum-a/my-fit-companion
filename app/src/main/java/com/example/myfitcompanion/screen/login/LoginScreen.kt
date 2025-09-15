package com.example.myfitcompanion.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.components.VerticalSpace
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidEmail
import com.example.myfitcompanion.utils.isValidPassword
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSucceed: () -> Unit = {}
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf(false) }
        var passwordError by remember { mutableStateOf(false) }

        EmailInput(
            email = email,
            onEmailChange = { email = it },
            emailError = emailError,
            onEmailErrorChange = { emailError = it },
            modifier = modifier
        )

        VerticalSpace(modifier, 8.dp)

        PasswordInput(
            password = password,
            onPasswordChange = { password = it },
            passwordError = passwordError,
            onPasswordErrorChange = { passwordError = it }
        )

        VerticalSpace(modifier, 16.dp)

        LoginButton(
            email = email,
            password = password,
            isLoading = loginState is ResultWrapper.Loading,
            onLoginClick = { emailValue, passwordValue ->
                viewModel.login(email = emailValue, password = passwordValue)
            }
        )

        VerticalSpace(modifier, 16.dp)

        LoginState(loginState, onLoginSucceed)
    }
}

@Composable
fun LoginState(
    loginState: ResultWrapper<UserResponse>,
    onLoginSucceed: () -> Unit = {}
) {
    when (loginState) {
        is ResultWrapper.Initial -> {}
        is ResultWrapper.Loading -> {}
        is ResultWrapper.Success -> {
            Text(
                text = "Login Successful",
                style = MaterialTheme.typography.bodyMedium
            )
            LaunchedEffect(loginState) {
                delay(300)
                onLoginSucceed()
            }
        }

        is ResultWrapper.Error -> {
            Text(
                text = loginState.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmailInput(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: Boolean,
    onEmailErrorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            onEmailErrorChange(false)
        },
        label = { Text("Email", color = Color.Gray) },
        isError = emailError,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onEmailErrorChange(
                    if (!focusState.isFocused && email.isNotEmpty()) {
                        !isValidEmail(email)
                    } else {
                        false
                    }
                )
            }
            .focusRequester(focusRequester),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (emailError) MaterialTheme.colorScheme.error else myFitColors.current.gold,
            unfocusedBorderColor = if (emailError) Color.Red else Color.Gray,
            cursorColor = myFitColors.current.gold,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
    )

    if (emailError) {
        Text(
            text = "Email is invalid",
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 2.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun PasswordInput(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: Boolean,
    onPasswordErrorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password", color = Color.Gray) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = myFitColors.current.gold,
            unfocusedBorderColor = Color.Gray,
            cursorColor = myFitColors.current.gold,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onPasswordErrorChange(
                    if (!focusState.isFocused && password.isNotEmpty()) {
                        !isValidPassword(password)
                    } else {
                        false
                    }
                )
            }
    )

    if (passwordError) {
        Text(
            text = "Password must be at least 6 characters, contain a number and a uppercase letter",
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 2.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun LoginButton(
    email: String,
    password: String,
    isLoading: Boolean,
    onLoginClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onLoginClick(email.trim(), password.trim()) },
        enabled = isValidEmail(email) && isValidPassword(password) && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = myFitColors.current.gold,
            contentColor = Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text("Login")
        }
    }
}
