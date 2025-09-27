package com.example.myfitcompanion.screen.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfitcompanion.db.room.entities.Meal
import com.example.myfitcompanion.ui.theme.myFitColors

/**
 * Created by Edon Idrizi on 19/Sep/2025 :)
 **/
@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    meals: List<Meal> = listOf(),
    onMealClick: (Meal) -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp)
    ) {
        if (meals.isEmpty()) {
            Text(
                text = "No meals available",
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
                items(meals) { meal ->
                    MealCard(
                        meal = meal,
                        onClick = { onMealClick(meal) }
                    )
                }
            }
        }
    }
}

@Composable
fun MealCard(
    meal: Meal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            myFitColors.current.orange.copy(alpha = 0.8f),
                            myFitColors.current.yellow.copy(alpha = 0.6f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Meal",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = meal.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Text(
                    text = "${meal.calories} calories",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = myFitColors.current.gold,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                meal.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        ),
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TestMealScreen() {
    val meal = listOf(
        Meal(1, "Chicken Salad", 350, "A healthy chicken salad with mixed greens and vinaigrette."),
        Meal(2, "Grilled Salmon", 450, "Grilled salmon with lemon butter sauce and steamed vegetables."),
        Meal(3, "Veggie Stir-fry", 300, "A colorful veggie stir-fry with tofu and brown rice."),
        Meal(4, "Beef Tacos", 500, "Spicy beef tacos with fresh salsa and guacamole."),
        Meal(5, "Pasta Primavera", 400, "Pasta with fresh vegetables in a light garlic sauce."),
        Meal(6, "Fruit Smoothie", 250, "A refreshing fruit smoothie with yogurt and honey."))

    MealScreen(meals = meal) {

    }
}