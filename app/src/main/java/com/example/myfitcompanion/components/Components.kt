package com.example.myfitcompanion.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.delay
import java.io.File

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
    modifier: Modifier = Modifier,
    input: String,
    label: String,
    onInputChange: (String) -> Unit,
    inputError: Boolean = false,
    onInputErrorChange: (Boolean) -> Unit = {},
    shouldFocus: Boolean = false,
    errorMessage: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isValid: (input: String) -> Boolean = { false }
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
        keyboardOptions = keyboardOptions,
        label = { Text(label, color = Color.Gray) },
        isError = inputError,
        visualTransformation = visualTransformation,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onInputErrorChange(
                    if (!focusState.isFocused && input.isNotEmpty()) {
                        !isValid(input)
                    } else {
                        false
                    }
                )
            }
            .focusRequester(focusRequester),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (inputError) MaterialTheme.colorScheme.error else myFitColors.current.gold,
            unfocusedBorderColor = if (inputError) MaterialTheme.colorScheme.error else Color.Gray,
            cursorColor = myFitColors.current.gold,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
    )

    if (inputError) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 2.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SubmitButton(
    text: String,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
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
                strokeWidth = 2.dp,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text(text)
        }
    }
}

@Composable
fun UserResponse(
    state: ResultWrapper<UserResponse>,
    delay: Long,
    onSucceed: (isAdmin: Boolean) -> Unit = {}
) {
    when (state) {
        is ResultWrapper.Initial -> {}
        is ResultWrapper.Loading -> {}
        is ResultWrapper.Success -> {
            LaunchedEffect(state) {
                delay(delay)
                onSucceed(state.data.role == "admin".uppercase())
            }
        }

        is ResultWrapper.Error -> {
            Text(
                text = state.message ?: "Unknown error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ImagePickerHandler(
    onImageSelected: (Uri) -> Unit,
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    var showImageSourceDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) { onImageSelected(cameraImageUri) }
    }

    // ✅ Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Launch camera after permission granted
            val photoFile = File.createTempFile("profile_photo", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onImageSelected(it) }
    }

    // Content with click handler
    content { showImageSourceDialog = true }

    // Image source selection dialog
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Select Image Source") },
            text = { Text("Choose how you want to add your profile picture") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Text("Gallery")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        // ✅ Check permission first
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                val photoFile = File.createTempFile("profile_photo", ".jpg", context.cacheDir)
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
                                cameraImageUri = uri
                                cameraLauncher.launch(uri)
                            }
                            else -> {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }
                ) {
                    Text("Camera")
                }
            }
        )
    }
}
