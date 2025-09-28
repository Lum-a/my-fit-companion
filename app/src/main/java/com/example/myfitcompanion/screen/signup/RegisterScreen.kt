package com.example.myfitcompanion.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.example.myfitcompanion.components.VerticalSpace
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.components.SubmitButton
import com.example.myfitcompanion.components.UserResponse
import com.example.myfitcompanion.components.UserTextField
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidEmail
import com.example.myfitcompanion.utils.isValidPassword

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSucceed: (Boolean) -> Unit = {},
    onLogin: () -> Unit = {},
) {
    val state by viewModel.registerState.collectAsStateWithLifecycle()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium.copy(color = myFitColors.current.gold))

        UserTextField(
            modifier = modifier,
            label = "First Name",
            input = firstName,
            onInputChange = { firstName = it },
            onInputErrorChange = { firstNameError = it },
            inputError = firstNameError,
            errorMessage = stringResource(R.string.invalid_name),
            isValid = { firstName.isNotEmpty() },
            shouldFocus = true
        )

        UserTextField(
            modifier = modifier,
            label = "Last Name",
            input = lastName,
            onInputChange = { lastName = it },
            onInputErrorChange = { lastNameError = it },
            inputError = lastNameError,
            errorMessage = stringResource(R.string.invalid_name),
            isValid = { lastName.isNotEmpty() }
        )

        UserTextField(
            modifier = modifier,
            label = "Email",
            input = email,
            onInputChange = { email = it },
            errorMessage = stringResource(R.string.invalid_email),
            onInputErrorChange = { emailError = it },
            inputError = emailError,
            isValid = { isValidEmail(email) }
        )

        UserTextField(
            modifier = modifier,
            label = "Password",
            input = password,
            onInputChange = { password = it },
            errorMessage = stringResource(R.string.invalid_password),
            onInputErrorChange = { passwordError = it },
            inputError = passwordError,
            isValid = { isValidPassword(password) },
            visualTransformation = PasswordVisualTransformation()
        )

        UserTextField(
            modifier = modifier,
            label = "Height(cm)",
            input = height,
            onInputChange = { height = it },
        )

        UserTextField(
            modifier = modifier,
            label = "Weight(kg)",
            input = weight,
            onInputChange = { weight = it },
        )

        UserTextField(
            modifier = modifier,
            label = "BodyFat",
            input = bodyFat,
            onInputChange = { bodyFat = it },
        )
        UserTextField(
            modifier = modifier,
            label = "Goal",
            input = goal,
            onInputChange = { goal = it },
        )

        VerticalSpace(modifier, 16.dp)

        SubmitButton(
            modifier = modifier,
            text = "Register",
            isLoading = state is ResultWrapper.Loading,
            enabled = isValidEmail(email) && isValidPassword(password) && firstName.isNotEmpty() && lastName.isNotEmpty(),
            onClick = {
                viewModel.register(
                    RegisterRequest(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        password = password,
                        height = height.toFloatOrNull(),
                        weight = weight.toFloatOrNull(),
                        bodyFat = bodyFat.toFloatOrNull(),
                        goal = goal.toFloatOrNull()
                    )
                )
            }
        )

        UserResponse(
            state = state,
            delay = 200,
            onSucceed = { onRegisterSucceed(it) }
        )

        VerticalSpace(modifier, 8.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Already have an account? ", color = Color.White)
            Text(
                text = "Sign in",
                color = myFitColors.current.gold,
                modifier = Modifier
                    .clickable { onLogin() }
                    .padding(8.dp)
            )
        }
    }
}
