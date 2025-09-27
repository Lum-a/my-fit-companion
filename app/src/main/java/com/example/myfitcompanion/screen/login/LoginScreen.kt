package com.example.myfitcompanion.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.R
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.components.SubmitButton
import com.example.myfitcompanion.components.UserResponse
import com.example.myfitcompanion.components.UserTextField
import com.example.myfitcompanion.components.VerticalSpace
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidEmail

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSucceed: (isAdmin: Boolean) -> Unit = {},
    onSignUp: () -> Unit = {},
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

        UserTextField(
            modifier = modifier,
            label = "Email",
            input = email,
            onInputChange = { email = it },
            inputError = emailError,
            onInputErrorChange = { emailError = it },
            shouldFocus = true,
            errorMessage = stringResource(R.string.invalid_email),
            isValid = { isValidEmail(email)}
        )

        VerticalSpace(modifier, 8.dp)

        UserTextField(
            modifier = modifier,
            label = "Password",
            input = password,
            onInputChange = { password = it },
            inputError = passwordError,
            errorMessage = stringResource(R.string.invalid_password),
            visualTransformation = PasswordVisualTransformation()
        )

        VerticalSpace(modifier, 16.dp)

        SubmitButton(
            text = "Login",
            enabled = isValidEmail(email) && password.isNotEmpty(),
            isLoading = loginState is ResultWrapper.Loading,
            onClick = { viewModel.login(LoginRequest(email = email, password = password)) }
        )

        VerticalSpace(modifier, 16.dp)

        UserResponse(
            state = loginState,
            delay = 200,
            onSucceed = { onLoginSucceed(it) }
        )

        VerticalSpace(modifier, 8.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Don't have an account? ", color = Color.White)
            Text(
                text = "Sign up",
                color = myFitColors.current.gold,
                modifier = Modifier
                    .clickable { onSignUp() }
                    .padding(8.dp)
            )
        }
    }
}