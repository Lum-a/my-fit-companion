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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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