package com.example.myfitcompanion.screen.meal

import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.db.room.entities.Meal
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

/**
 * Created by Edon Idrizi on 25/Sep/2025 :)
 **/
interface MealRepository {

    suspend fun insertMeal(meal: Meal)
    suspend fun getMealsFromApi(): ResultWrapper<List<MealsResponse>>
    fun getAllMeals(): Flow<List<Meal>>
    suspend fun syncMeals(): ResultWrapper<Unit>
}