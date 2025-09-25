package com.example.myfitcompanion.screen.meal

import android.util.Log
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.db.room.dao.MealDao
import com.example.myfitcompanion.db.room.entities.Meal
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Edon Idrizi on 25/Sep/2025 :)
 **/

@Singleton
class MealRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mealDao: MealDao
): MealRepository {

    override suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    override suspend fun getMealsFromApi(): ResultWrapper<List<MealsResponse>> = try {
            val response = apiService.getMeals()
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            Log.e("MealRepositoryImpl", "Error getting meals from API: ${e.message}")
            ResultWrapper.Error(e.message)
        }

    override fun getAllMeals(): Flow<List<Meal>> {
        return mealDao.getAllMeals()
    }

    override suspend fun syncMeals(): ResultWrapper<Unit> = try {
        when (val result = getMealsFromApi()) {
            is ResultWrapper.Success -> {
                Log.d("MealRepositoryImpl", "meals: ${result.data}")
                result.data.forEach { mealResponse ->
                    val meal = Meal(
                        name = mealResponse.name,
                        calories = mealResponse.calories,
                        description = mealResponse.description
                    )
                    insertMeal(meal)
                }
                ResultWrapper.Success(Unit)
            }
            is ResultWrapper.Error -> {
                Log.e("MealRepositoryImpl", "Error syncing meals: ${result.message}")
                ResultWrapper.Error(result.message)
            }
            else -> ResultWrapper.Error("Unknown error occurred")
        }
    } catch (e: Exception) {
        Log.e("MealRepositoryImpl", "Error syncing meals: ${e.message}")
        ResultWrapper.Error(e.message)
    }

}
