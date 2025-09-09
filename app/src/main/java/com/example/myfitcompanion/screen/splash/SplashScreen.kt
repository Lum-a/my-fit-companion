package com.example.myfitcompanion.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.example.myfitcompanion.R
import com.example.myfitcompanion.ui.theme.darkBackground
import com.example.myfitcompanion.utils.AuthViewModel

@Composable
@Preview
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.myfit_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1 // play once
    )
    var showButtons by remember { mutableStateOf(false) }

    // Navigate after animation finishes if logged in, else show buttons
    LaunchedEffect(progress, isLoggedIn) {
        if (progress >= 1f) {
            if (isLoggedIn == true) {
                onNavigateToHome()
            } else {
                showButtons = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBackground),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = modifier.size(500.dp),
            composition = composition,
            progress = progress
        )
        if (showButtons) {
            // Define gold color
            val Gold = Color(0xFFFFD700)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp)
            ) {
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Log In")
                }
                Button(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth(0.7f).padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Register")
                }
            }
        }
    }

}