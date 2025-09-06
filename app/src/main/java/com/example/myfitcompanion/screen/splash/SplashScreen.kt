package com.example.myfitcompanion.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myfitcompanion.R
import com.example.myfitcompanion.ui.theme.darkBackground

@Composable
@Preview
fun SplashScreen(modifier: Modifier = Modifier, onFinished: () -> Unit = {}) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.myfit_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1 // play once
    )

    // Navigate after animation finishes
    LaunchedEffect(progress) {
        if (progress >= 1f) {
            onFinished()
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