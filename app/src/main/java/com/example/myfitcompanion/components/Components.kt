package com.example.myfitcompanion.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalSpace(modifier: Modifier = Modifier, height: Dp) {
    Spacer(modifier.height(height))
}

@Composable
fun HorizontalSpace(modifier: Modifier = Modifier, width: Dp) {
    Spacer(modifier.width(width))
}