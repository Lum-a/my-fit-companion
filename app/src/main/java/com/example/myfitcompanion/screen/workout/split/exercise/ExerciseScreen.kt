package com.example.myfitcompanion.screen.workout.split.exercise

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.screen.workout.WorkoutViewModel
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

/**
 * Created by Edon Idrizi on 19/Sep/2025 :)
 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    splitId: Int,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onExerciseClick: (ExerciseResponse) -> Unit = {}
) {
    val exerciseState by viewModel.exerciseState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
    ) {
        TopAppBar(
            title = { Text("Exercises", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { onBack() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = myFitColors.current.background,
                titleContentColor = myFitColors.current.gold
            )
        )

        when (val exercises = exerciseState) {
            is ResultWrapper.Initial -> {
                LaunchedEffect(splitId) {
                    viewModel.loadExercises(splitId)
                }
            }

            is ResultWrapper.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = myFitColors.current.orange)
                }
            }

            is ResultWrapper.Success -> {
                if (exercises.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No exercises available",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(exercises.data) { exercise: ExerciseResponse ->
                            ExerciseCard(
                                exercise = exercise,
                                onClick = { onExerciseClick(exercise) }
                            )
                        }
                    }
                }
            }

            is ResultWrapper.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = exercises.message ?: "Error loading exercises",
                        color = Color.White
                    )
                }
            }
        }

    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseResponse,
    onClick: () -> Unit
) {
    val thumbnailUrl = "https://img.youtube.com/vi/${exercise.videoId}/hqdefault.jpg"

    Card(
        modifier = Modifier
            .size(300.dp)
            .clickable(enabled = exercise.videoId.isNotEmpty()) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (exercise.videoId.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        tint = myFitColors.current.gold,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = exercise.description ?: "",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
            }
        }
    }
}
