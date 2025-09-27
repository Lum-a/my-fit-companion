package com.example.myfitcompanion.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onNavigateToLogin: () -> Unit = {}
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.myfit_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1 // play once
    )
    var navigated by remember { mutableStateOf(false) }

    // Navigate after animation finishes
    LaunchedEffect(progress, isLoggedIn) {
        if (progress >= 1f && !navigated) {
            navigated = true
            if (isLoggedIn) {
                onNavigateToHome()
            } else {
                onNavigateToLogin()
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
    }

}