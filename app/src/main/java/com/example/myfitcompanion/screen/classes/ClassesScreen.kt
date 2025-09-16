package com.example.myfitcompanion.screen.classes

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.myfitcompanion.R
import com.example.myfitcompanion.ui.theme.myFitColors

@Preview
@Composable
fun ClassesScreen() {

}
@Preview
@Composable
fun ClassesScreenTest(
    modifier: Modifier = Modifier,
    classes: List<FitnessClass> = sampleClasses,
    onClassClick: (FitnessClass) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background) // dark background
            .padding(16.dp)
    ) {
        Text(
            text = "Workout Classes",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(classes) { fitnessClass ->
                FitnessClassCard(fitnessClass, onClassClick)
            }
        }
    }
}

@Composable
fun FitnessClassCard(
    fitnessClass: FitnessClass,
    onClick: (FitnessClass) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick(fitnessClass) },
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey), // dark card
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Gradient icon background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(myFitColors.current.orange, myFitColors.current.yellow) // orange → yellow
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = fitnessClass.icon,
                    contentDescription = null,
                    tint = Color.Black, // contrast against gradient
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = fitnessClass.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = fitnessClass.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = myFitColors.current.lightOrange), // light orange
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class FitnessClass(
    val name: String,
    val description: String,
    val icon: ImageVector
)

val sampleClasses = listOf(
    FitnessClass("CrossFit", "High intensity training", Icons.Default.Favorite),
    FitnessClass("Yoga", "Relax & stretch your body", Icons.Default.AccountBox),
    FitnessClass("Weight Lifting", "Strength training with weights", Icons.Default.Call),
    FitnessClass("Cardio", "Boost stamina & endurance", Icons.Default.AddCircle),
    FitnessClass("Pilates", "Core strengthening workout", Icons.Default.Favorite),
    FitnessClass("Boxing", "Punch and burn calories", Icons.Default.ThumbUp)
)


@Preview
@Composable
fun WorkoutsScreen(
    onWorkoutClick: (String) -> Unit = {}
) {

    val workouts = listOf(
        Workout("Cardio", "https://images.pexels.com/photos/1552249/pexels-photo-1552249.jpeg"),
        Workout("Weightlifting", "https://images.pexels.com/photos/2261485/pexels-photo-2261485.jpeg"),
        Workout("Yoga", "https://images.pexels.com/photos/3822622/pexels-photo-3822622.jpeg"),
        Workout("HIIT", "https://images.pexels.com/photos/841130/pexels-photo-841130.jpeg"),
        Workout("Pilates", "https://images.pexels.com/photos/4324026/pexels-photo-4324026.jpeg"),
        Workout("CrossFit", "https://images.pexels.com/photos/1552106/pexels-photo-1552106.jpeg"),
        Workout("Dance", "https://images.pexels.com/photos/3771069/pexels-photo-3771069.jpeg"),
        Workout("Boxing", "https://images.pexels.com/photos/4761353/pexels-photo-4761353.jpeg"),
        Workout("Stretching", "https://images.pexels.com/photos/4056723/pexels-photo-4056723.jpeg"),
        Workout("Meditation", "https://images.pexels.com/photos/3822621/pexels-photo-3822621.jpeg")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCard(workout, onClick = { onWorkoutClick(workout.name) })
            }
        }
    }
}

data class Workout(
    val name: String,
    val imageUrl: String
)

@Composable
fun WorkoutCard(workout: Workout, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
            // Coil with loading / error handling
            SubcomposeAsyncImage(
                model = workout.imageUrl,
                contentDescription = workout.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        // Loader while image is loading
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(myFitColors.current.cardsGrey),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = myFitColors.current.gold,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        // Show fallback if loading fails
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(myFitColors.current.cardsGrey),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.myfit_logo),
                                contentDescription = "Image load failed",
                                tint = Color.Gray,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    else -> {
                        // Image loaded successfully
                        SubcomposeAsyncImageContent()
                    }
                }
            }

            // Gradient overlay (for text readability)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            // Workout title
            Text(
                text = workout.name,
                color = myFitColors.current.gold,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}
