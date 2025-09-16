package com.example.myfitcompanion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.isValidEmail

@Composable
fun VerticalSpace(modifier: Modifier = Modifier, height: Dp) {
    Spacer(modifier.height(height))
}

@Composable
fun HorizontalSpace(modifier: Modifier = Modifier, width: Dp) {
    Spacer(modifier.width(width))
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFFA726), Color(0xFFFFEB3B)) // orange → yellow
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )
        }
    }
}

@Composable
fun UserTextField(
    input: String,
    label: String,
    onInputChange: (String) -> Unit,
    inputError: Boolean,
    onInputErrorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shouldFocus: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }

    if(shouldFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    OutlinedTextField(
        value = input,
        onValueChange = {
            onInputChange(it)
            onInputErrorChange(false)
        },
        label = { Text(label, color = Color.Gray) },
        isError = inputError,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onInputErrorChange(
                    if (!focusState.isFocused && input.isNotEmpty()) {
                        !isValidEmail(input )
                    } else {
                        false
                    }
                )
            }
            .focusRequester(focusRequester),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (inputError) MaterialTheme.colorScheme.error else myFitColors.current.gold,
            unfocusedBorderColor = if (inputError) Color.Red else Color.Gray,
            cursorColor = myFitColors.current.gold,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
    )

    if (inputError) {
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