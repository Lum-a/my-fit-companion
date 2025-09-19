package com.example.myfitcompanion.screen.session

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.myfitcompanion.R
import com.example.myfitcompanion.api.model.SessionResponse
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@Composable
fun SessionScreen(
    viewModel: SessionViewModel = hiltViewModel(),
    onSessionClick: (SessionResponse) -> Unit = {}
) {
    val sessionsState by viewModel.sessionsState.collectAsStateWithLifecycle()

    // Load sessions when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadSessions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp)
    ) {
        when (val sessions = sessionsState) {
            is ResultWrapper.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = myFitColors.current.orange
                )
            }
            is ResultWrapper.Success -> {
                if (sessions.data.isEmpty()) {
                    Text(
                        text = "No sessions available",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(sessions.data) { session ->
                            SessionCard(
                                session = session,
                                onClick = {
                                    onSessionClick(session)
                                    viewModel.addRecentSession(session.id)
                                }
                            )
                        }
                    }
                }
            }
            is ResultWrapper.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Failed to load sessions",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = sessions.message ?: "Unknown error",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
            is ResultWrapper.Initial -> {
                // Initial state - show nothing or placeholder
            }
        }
    }
}

@Composable
fun SessionCard(
    session: SessionResponse,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Session image (if available)
            session.imageUrl?.let { imageUrl ->
                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = session.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = myFitColors.current.orange,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        is AsyncImagePainter.State.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(myFitColors.current.cardsGrey),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "Session image",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        else -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            } ?: run {
                // Fallback gradient background if no image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                listOf(myFitColors.current.orange, myFitColors.current.yellow)
                            )
                        )
                )
            }

            // Overlay with session info
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            // Session details
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Session name
                Text(
                    text = session.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                // Session details
                Column {
                    session.date?.let { date ->
                        Text(
                            text = "Date: $date",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    session.duration?.let { duration ->
                        Text(
                            text = "Duration: $duration min",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                        )
                    }
                }
            }
        }
    }
}

// Keep the preview with sample data for design purposes
@Preview
@Composable
fun SessionScreenPreview() {
    // Sample data for preview
    val sampleSessions = listOf(
        SessionResponse(
            id = 1,
            name = "Morning Cardio",
            date = "2025-09-19",
            duration = 45,
            imageUrl = "https://images.pexels.com/photos/1552249/pexels-photo-1552249.jpeg",
            userId = 1
        ),
        SessionResponse(
            id = 2,
            name = "Strength Training",
            date = "2025-09-18",
            duration = 60,
            imageUrl = "https://images.pexels.com/photos/2261485/pexels-photo-2261485.jpeg",
            userId = 2
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sampleSessions) { session ->
                SessionCard(
                    session = session,
                    onClick = { }
                )
            }
        }
    }
}
