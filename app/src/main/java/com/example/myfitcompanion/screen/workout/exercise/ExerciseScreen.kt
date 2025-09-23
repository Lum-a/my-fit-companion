package com.example.myfitcompanion.screen.workout.exercise

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.screen.workout.WorkoutViewModel
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper
import androidx.core.net.toUri

fun isYouTubeUrl(url: String?): Boolean {
    return url?.contains("youtube.com") == true || url?.contains("youtu.be") == true
}

@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl.toUri()))
            prepare()
            playWhenReady = true
        }
    }
   VideoPlayerComposable(exoPlayer)
}

@Composable
fun VideoPlayerComposable(exoPlayer: ExoPlayer, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                // You can set other properties here, e.g., resizeMode
            }
        },
        update = { view ->
            // Update properties if needed when the Composable recomposes
            view.player = exoPlayer
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    splitId: Int,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onExerciseClick: (ExerciseResponse) -> Unit = {}
) {
    val exerciseState by viewModel.exerciseState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showVideoPlayer = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
    ) {
        TopAppBar(
            title = { Text("Exercises", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = {
                    if (showVideoPlayer.value != null) {
                        showVideoPlayer.value = null // Close video player if open
                    } else {
                        onBackClick()
                    }
                }) {
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

        if (showVideoPlayer.value != null) {
            // Only show video player, hide grid
            VideoPlayer(videoUrl = showVideoPlayer.value!!)
        } else {
            // Show grid of exercise cards
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
                                    onClick = {
                                        val videoUrl: String? = exercise.videoUrl
                                        if (videoUrl != null && videoUrl.isNotEmpty()) {
                                            if (isYouTubeUrl(videoUrl)) {
                                                val intent = Intent(Intent.ACTION_VIEW, videoUrl.toUri())
                                                intent.setPackage("com.google.android.youtube")
                                                try {
                                                    context.startActivity(intent)
                                                } catch (e: ActivityNotFoundException) {
                                                    context.startActivity(Intent(Intent.ACTION_VIEW,
                                                        videoUrl.toUri()))
                                                }
                                            } else {
                                                showVideoPlayer.value = videoUrl
                                            }
                                        }
                                    }
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
}

@Composable
fun ExerciseCard(
    exercise: ExerciseResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clickable(enabled = exercise.videoUrl != null && exercise.videoUrl.isNotEmpty()) {
                onClick()

                                                                                              },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Show thumbnail image if available
                AsyncImage(
                    model = exercise.thumbnailUrl,
                    contentDescription = exercise.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

            // Overlay play icon if videoUrl exists
            if (!exercise.videoUrl.isNullOrEmpty()) {
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
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
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

@Composable
fun ExerciseTestScreen() {
    val showVideoPlayer = remember { mutableStateOf<String?>(null) }

    val testExercise = ExerciseResponse(
        id = 1,
        name = "Test MP4 Video",
        description = "A direct MP4 video test.",
        videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
        thumbnailUrl = "https://www.w3schools.com/html/pic_trulli.jpg"
    )
    var showThumbnail by remember { mutableStateOf(testExercise.thumbnailUrl?.isNotEmpty() == true) }

    showVideoPlayer.value?.let { videoUrl ->
        VideoPlayer(videoUrl = videoUrl)
    }

    if(showThumbnail) {
        ExerciseCard(
            exercise = testExercise,
            onClick = {
                showThumbnail = false
                Log.d("ExerciseCardTest", "Exercise clicked")
                showVideoPlayer.value = testExercise.videoUrl
            }
        )
    }
}
