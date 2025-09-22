package com.example.myfitcompanion.screen.profile

import android.net.Uri
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myfitcompanion.R
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.components.ImagePickerHandler
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onChangePassword: () -> Unit = {}
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val imageUploadState by viewModel.imageUploadState.collectAsStateWithLifecycle()

    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var uri by remember { mutableStateOf<Uri?>(null) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }

    // Initialize fields when user data loads
    LaunchedEffect(user) {
        user?.let {
            firstName = it.firstName ?: ""
            lastName = it.lastName ?: ""
            height = it.height?.toString() ?: ""
            weight = it.weight?.toString() ?: ""
            bodyFat = it.bodyFat?.toString() ?: ""
            goal = it.goal ?: ""
        }
    }

    // Handle image upload result
    LaunchedEffect(imageUploadState) {
        when (val imageState = imageUploadState) {
            is ResultWrapper.Success -> {
                uploadedImageUrl = imageState.data
            }
            is ResultWrapper.Error -> {
                // Handle error
            }
            else -> { /* Handle other states */ }
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

        // Profile photo with image picker - SIMPLIFIED
        ImagePickerHandler(
            onImageSelected = { newUri ->
                uri = newUri
            }
        ) { onClick ->
            Box(modifier = Modifier.size(120.dp)) {
                AsyncImage(
                    model = uri ?: user?.imageUrl,
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

                // Show loading indicator during image upload
                if (imageUploadState is ResultWrapper.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = myFitColors.current.gold
                    )
                }

                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape),
                    enabled = imageUploadState !is ResultWrapper.Loading
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Change Photo",
                        tint = myFitColors.current.gold
                    )
                }
            }
        }

        // Show image upload error if exists
        if (imageUploadState is ResultWrapper.Error) {
            Text(
                text = "Image upload failed: ${(imageUploadState as ResultWrapper.Error).message}",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name", color = Color.Gray) },
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
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name", color = Color.Gray) },
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
//        OutlinedTextField(
//            value = password,
//            onValueChange = {
//                password = it
//                passwordError = it.isNotEmpty() && !isValidPassword(it)
//            },
//            label = { Text("New Password", color = Color.Gray) },
//            singleLine = true,
//            visualTransformation = PasswordVisualTransformation(),
//            isError = passwordError,
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = if (passwordError) Color.Red else myFitColors.current.gold,
//                unfocusedBorderColor = if (passwordError) Color.Red else Color.Gray,
//                cursorColor = myFitColors.current.gold,
//                focusedTextColor = Color.White,
//                unfocusedTextColor = Color.White
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )

        Button(
            onClick = {
                viewModel.updateProfile(
                    UpdateProfileRequest(
                        firstName = firstName.ifBlank { null },
                        lastName = lastName.ifBlank { null },
                        height = height.toFloatOrNull(),
                        weight = weight.toFloatOrNull(),
                        bodyFat = bodyFat.toFloatOrNull(),
                        goal = goal.ifBlank { null },
                        imageUrl = uploadedImageUrl
                    )
                )
                uri?.let { viewModel.uploadProfileImage(it) }
            },
            enabled = updateState !is ResultWrapper.Loading,
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

    }
}
