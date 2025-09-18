package com.example.myfitcompanion.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myfitcompanion.R
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidPassword

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onChangePassword: (() -> Unit)? = null // Add navigation lambda
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()

    var photoUrl by remember { mutableStateOf(user?.photoUrl ?: "") }
    var height by remember { mutableStateOf(user?.height?.toString() ?: "") }
    var weight by remember { mutableStateOf(user?.weight?.toString() ?: "") }
    var bodyFat by remember { mutableStateOf(user?.bodyFat?.toString() ?: "") }
    var goal by remember { mutableStateOf(user?.goal ?: "") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    // Update fields when user changes (e.g. after update)
    LaunchedEffect(user) {
        photoUrl = user?.photoUrl ?: ""
        height = user?.height?.toString() ?: ""
        weight = user?.weight?.toString() ?: ""
        bodyFat = user?.bodyFat?.toString() ?: ""
        goal = user?.goal ?: ""
    }

    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri != null) {
            // TODO: Upload to blob and update photoUrl
            viewModel.uploadProfilePhoto(cameraImageUri!!, context)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium.copy(color = myFitColors.current.gold))
        Spacer(modifier = Modifier.height(16.dp))
        // Profile photo with camera button overlay
        Box(modifier = Modifier.size(120.dp)) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                onError = { /* Optionally handle error, e.g. show a placeholder */ },
                onSuccess = { /* Optionally handle success */ }
            )
            IconButton(
                onClick = {
                    // Create a temp file for the camera image
                    val photoFile = File.createTempFile("profile_photo", ".jpg", context.cacheDir)
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
                    cameraImageUri = uri
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Take Photo",
                    tint = myFitColors.current.gold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = photoUrl,
            onValueChange = {
                photoUrl = it
                viewModel.updatePhotoUrl(it)
            },
            label = { Text("Photo URL", color = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = myFitColors.current.gold,
                unfocusedBorderColor = Color.Gray,
                cursorColor = myFitColors.current.gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)", color = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = myFitColors.current.gold,
                unfocusedBorderColor = Color.Gray,
                cursorColor = myFitColors.current.gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)", color = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = myFitColors.current.gold,
                unfocusedBorderColor = Color.Gray,
                cursorColor = myFitColors.current.gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = bodyFat,
            onValueChange = { bodyFat = it },
            label = { Text("Body Fat (%)", color = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = myFitColors.current.gold,
                unfocusedBorderColor = Color.Gray,
                cursorColor = myFitColors.current.gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            label = { Text("Goal", color = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = myFitColors.current.gold,
                unfocusedBorderColor = Color.Gray,
                cursorColor = myFitColors.current.gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = it.isNotEmpty() && !isValidPassword(it)
            },
            label = { Text("New Password", color = Color.Gray) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (passwordError) Color.Red else myFitColors.current.gold,
                unfocusedBorderColor = if (passwordError) Color.Red else Color.Gray,
                cursorColor = myFitColors.current.gold,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError && password.isNotEmpty()) {
            Text(
                text = "Password must be at least 8 characters, include uppercase, lowercase, and a number",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, top = 2.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (val state = updateState) {
            is ResultWrapper.Initial -> {}
            is ResultWrapper.Loading -> CircularProgressIndicator(color = myFitColors.current.gold)
            is ResultWrapper.Error -> Text(text = "Error: ${state.message}", color = Color.Red)
            is ResultWrapper.Success<*> -> Text(text = "Profile updated!", color = myFitColors.current.gold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.updateUserInfo(
                    com.example.myfitcompanion.api.model.UpdateProfileRequest(
                        height = height.toFloatOrNull(),
                        weight = weight.toFloatOrNull(),
                        bodyFat = bodyFat.toFloatOrNull(),
                        goal = goal.ifBlank { null },
//                        password = if (password.isNotBlank()) password else null,
                        photoUrl = photoUrl
                    )
                )
            },
            enabled = !passwordError && (height.isNotBlank() || weight.isNotBlank() || bodyFat.isNotBlank() || goal.isNotBlank() || password.isNotBlank() || photoUrl.isNotBlank()),
            colors = ButtonDefaults.buttonColors(
                containerColor = myFitColors.current.gold,
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Add Change Password button
        Button(
            onClick = { onChangePassword?.invoke() },
            colors = ButtonDefaults.buttonColors(
                containerColor = myFitColors.current.gold,
                contentColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Password")
        }
    }
}