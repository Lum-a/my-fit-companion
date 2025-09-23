package com.example.myfitcompanion.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myfitcompanion.R
import com.example.myfitcompanion.ui.theme.myFitColors

@Composable
fun SettingsScreen(
    onChangeEmail: () -> Unit,
    onChangePassword: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp)
    ) {
        // Header with back button - moved to top
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = myFitColors.current.gold
                )
            }
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(color = myFitColors.current.gold)
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
        }

        // Center the buttons vertically in remaining space
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Change Email Button
            Button(
                onClick = onChangeEmail,
                colors = ButtonDefaults.buttonColors(
                    containerColor = myFitColors.current.gold,
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Email", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Change Password Button
            Button(
                onClick = onChangePassword,
                colors = ButtonDefaults.buttonColors(
                    containerColor = myFitColors.current.gold,
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Password", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
