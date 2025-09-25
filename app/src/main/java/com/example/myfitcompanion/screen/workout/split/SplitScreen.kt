package com.example.myfitcompanion.screen.workout.split

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myfitcompanion.api.model.SplitResponse
import com.example.myfitcompanion.screen.workout.WorkoutViewModel
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitScreen(
    workoutId: Int,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onSplitClick: (Int) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val splitsState by viewModel.splitState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
    ) {
        // Header with back button
        TopAppBar(
            title = { Text("Workout Splits", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = myFitColors.current.background,
                titleContentColor = myFitColors.current.gold
            )
        )

        when (val splits = splitsState) {

            is ResultWrapper.Initial -> {
                LaunchedEffect(workoutId) {
                    viewModel.loadSplits(workoutId)
                }
            }

            is ResultWrapper.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = myFitColors.current.orange)
                }
            }

            is ResultWrapper.Success -> {
                LazyVerticalGrid(
                    columns = Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(splits.data) { split ->
                        WorkoutSplitCard(
                            split = split,
                            onClick = { onSplitClick(split.splitId) }
                        )
                    }
                }
            }

            is ResultWrapper.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = splits.message ?: "Error loading splits",
                        color = Color.White
                    )
                }
            }

        }
    }
}

@Composable
fun WorkoutSplitCard(
    split: SplitResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image or gradient
            split.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = split.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(myFitColors.current.orange, myFitColors.current.yellow)
                        )
                    )
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = split.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = split.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = myFitColors.current.gold
                    )
                )
            }
        }
    }
}
