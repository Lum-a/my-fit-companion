package com.example.myfitcompanion.screen.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
    onProfileUpdated: () -> Unit = {},
    onChangePassword: () -> Unit = {}
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()

    Log.d("ProfileScreen", "User data: $user")
    if(updateState is ResultWrapper.Success) {
        Toast.makeText(LocalContext.current, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        onProfileUpdated()
       Log.d("ProfileScreen", "Profile updated successfully: ${(updateState as ResultWrapper.Success).data}")
    }
    var uri by remember { mutableStateOf<Uri?>(null) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var goalBodyFat by remember { mutableStateOf("") }
    var goalWeight by remember { mutableStateOf("") }

    val hasChanges = user?.let { safeUser ->
        firstName != (safeUser.firstName ?: "") ||
                lastName != (safeUser.lastName ?: "") ||
                height != (safeUser.height?.toString() ?: "") ||
                weight != (safeUser.weight?.toString() ?: "") ||
                bodyFat != (safeUser.bodyFat?.toString() ?: "") ||
                goalBodyFat != (safeUser.goalBodyFat ?: "") ||
                goalWeight != (safeUser.goalWeight ?: "") ||
                uri != null
    } == true


    // Initialize fields when user data loads
    LaunchedEffect(user) {
        user?.let {
            firstName = it.firstName ?: ""
            lastName = it.lastName ?: ""
            height = it.height?.toString() ?: ""
            weight = it.weight?.toString() ?: ""
            bodyFat = it.bodyFat?.toString() ?: ""
            goalBodyFat = it.goalBodyFat?.toString() ?: ""
            goalWeight = it.goalWeight?.toString() ?: ""
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

        ImagePickerHandler(onImageSelected = { uri = it }) { onClick ->
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


                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape),
                    enabled = updateState !is ResultWrapper.Loading
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Change Photo",
                        tint = myFitColors.current.gold
                    )
                }
            }
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
            value = goalWeight,
            onValueChange = { goalWeight = it },
            label = { Text("Goal Weight(KG)", color = Color.Gray) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
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
            value = goalBodyFat,
            onValueChange = { goalBodyFat = it },
            label = { Text("Goal Body Fat (%)", color = Color.Gray) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
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

        Button(
            onClick = {
                user?.let { user ->
                    viewModel.updateProfile(
                        UpdateProfileRequest(
                            userId = user.id,
                            firstName = firstName.ifBlank { null },
                            lastName = lastName.ifBlank { null },
                            height = height.toFloatOrNull(),
                            weight = weight.toFloatOrNull(),
                            bodyFat = bodyFat.toFloatOrNull(),
                            goalWeight = goalBodyFat.toFloatOrNull(),
                            goalBodyFat = goalBodyFat.toFloatOrNull()
                        ),
                        imageUri = uri
                    )
                }

            },
            enabled = hasChanges && updateState !is ResultWrapper.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = myFitColors.current.gold,
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            if(updateState is ResultWrapper.Loading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save Changes")
            }
        }
    }
}
