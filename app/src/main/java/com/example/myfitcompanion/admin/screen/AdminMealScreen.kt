package com.example.myfitcompanion.admin.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myfitcompanion.api.model.MealRequest
import com.example.myfitcompanion.admin.viewmodel.AdminViewModel
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMealScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val mealsState by viewModel.meals.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingMeal by remember { mutableStateOf<MealsResponse?>(null) }
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Meals") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = myFitColors.current.background,
                    titleContentColor = myFitColors.current.gold
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingMeal = null
                    name = ""
                    calories = ""
                    description = ""
                    showDialog = true
                },
                containerColor = myFitColors.current.gold
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Meal")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = mealsState) {

                ResultWrapper.Initial -> {
                    LaunchedEffect(Unit) { viewModel.loadMeals() }
                }

                is ResultWrapper.Loading -> {
                    CircularProgressIndicator(color = myFitColors.current.gold)
                }
                is ResultWrapper.Error -> {
                    Text(
                        "Failed to load meals",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ResultWrapper.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { meal ->
                            MealCard(
                                meal = meal,
                                onEdit = {
                                    editingMeal = meal
                                    name = meal.name
                                    calories = meal.calories.toString()
                                    description = meal.description ?: ""
                                    showDialog = true
                                },
                                onDelete = { viewModel.deleteMeal(meal.id) }
                            )
                        }
                    }
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    editingMeal = null
                    name = ""
                    calories = ""
                    description = ""
                },
                confirmButton = {
                    TextButton(onClick = {
                        val caloriesInt = calories.toIntOrNull()
                        if (name.isNotBlank() && caloriesInt != null) {
                            val mealRequest = MealRequest(
                                name = name,
                                calories = caloriesInt,
                                description = description.ifBlank { null }
                            )

                            editingMeal?.let {
                                viewModel.updateMeal(it.id, mealRequest)
                            } ?: viewModel.addMeal(mealRequest)

                            showDialog = false
                            editingMeal = null
                            name = ""
                            calories = ""
                            description = ""
                        }
                    }) {
                        Text(if (editingMeal != null) "Update" else "Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        editingMeal = null
                        name = ""
                        calories = ""
                        description = ""
                    }) {
                        Text("Cancel")
                    }
                },
                title = { Text(if (editingMeal != null) "Edit Meal" else "Create Meal") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = calories,
                            onValueChange = { calories = it },
                            label = { Text("Calories") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun MealCard(
    meal: MealsResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(meal.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text("Calories: ${meal.calories}", color = myFitColors.current.gold, style = MaterialTheme.typography.bodySmall)
                meal.description?.let {
                    Text(it, color = myFitColors.current.lightOrange, style = MaterialTheme.typography.bodySmall)
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = myFitColors.current.yellow)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}
