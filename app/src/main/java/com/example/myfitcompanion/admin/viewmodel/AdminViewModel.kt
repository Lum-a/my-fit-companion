package com.example.myfitcompanion.admin.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.admin.repository.AdminRepository
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.api.model.CreateUserRequest
import com.example.myfitcompanion.api.model.UpdateUserRequest
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.MealRequest
import com.example.myfitcompanion.api.model.WorkoutResponse
import com.example.myfitcompanion.api.model.WorkoutRequest
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.ExerciseRequest
import com.example.myfitcompanion.api.model.SplitRequest
import com.example.myfitcompanion.api.model.SplitResponse
import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.api.model.UpdateTrainerRequest
import com.example.myfitcompanion.utils.AppWriteInteractor
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val appWriteInteractor: AppWriteInteractor
): ViewModel() {

    private val TAG = "AdminViewModel"
    // Users
    private val _users = MutableStateFlow<ResultWrapper<List<UserResponse>>>(ResultWrapper.Initial)
    val users = _users.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = ResultWrapper.Loading
            _users.value = adminRepository.getUsers()
        }
    }

    suspend fun getUserId(): Int? {
        return adminRepository.getUserId()
    }

    fun addUser(user: CreateUserRequest) {
        viewModelScope.launch {
            adminRepository.addUser(user)
            loadUsers()
        }
    }

    fun updateUser(userId: Int, user: UpdateUserRequest) {
        viewModelScope.launch {
            adminRepository.updateUser(userId, user)
            loadUsers()
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            adminRepository.deleteUser(userId)
            loadUsers()
        }
    }

    //Workouts
    private val _workouts = MutableStateFlow<ResultWrapper<List<WorkoutResponse>>>(ResultWrapper.Initial)
    val workouts = _workouts.asStateFlow()

    fun loadWorkouts() {
        viewModelScope.launch {
            _workouts.value = ResultWrapper.Loading
            _workouts.value = adminRepository.getWorkouts()
        }
    }

    fun addWorkout(workout: WorkoutRequest, uri: Uri?) {
        viewModelScope.launch {
            val updatedWorkout = workout.copy(imageUrl = getImageUrlFromBlob(uri))
            adminRepository.addWorkout(updatedWorkout)
            loadWorkouts()
        }
    }

    fun updateWorkout(workoutId: Int, workout: WorkoutRequest) {
        viewModelScope.launch {
            adminRepository.updateWorkout(workoutId, workout)
            loadWorkouts()
        }
    }

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch {
            adminRepository.deleteWorkout(workoutId)
            loadWorkouts()
        }
    }

    // Splits
    private val _splits = MutableStateFlow<ResultWrapper<List<SplitResponse>>>(ResultWrapper.Initial)
    val splits = _splits.asStateFlow()
    private val workoutId = 0

    fun loadSplits(id:  Int) {
        viewModelScope.launch {
            _splits.value = ResultWrapper.Loading
            _splits.value = adminRepository.getWorkoutSplits(id)
        }
    }

    fun addSplit(split: SplitRequest) {
        viewModelScope.launch {
            adminRepository.addWorkoutSplit(split)
            loadSplits(workoutId)
        }
    }

    fun updateSplit(splitId: Int, split: SplitRequest) {
        viewModelScope.launch {
            adminRepository.updateWorkoutSplit(splitId, split)
            loadSplits(workoutId)
        }
    }

    fun deleteSplit(splitId: Int) {
        viewModelScope.launch {
            adminRepository.deleteWorkoutSplit(splitId)
            loadSplits(workoutId)
        }
    }

    // Exercises
    private val _exercises = MutableStateFlow<ResultWrapper<List<ExerciseResponse>>>(ResultWrapper.Initial)
    val exercises = _exercises.asStateFlow()
    private var splitId = 0
    fun loadExercises(id: Int) {
        viewModelScope.launch {
            _exercises.value = ResultWrapper.Loading
            _exercises.value = adminRepository.getExercises(id)
            splitId = id
        }
    }

    fun addExercise(exercise: ExerciseRequest) {
        viewModelScope.launch {
            adminRepository.addExercise(exercise)
            loadExercises(splitId)
        }
    }

    fun updateExercise(exerciseId: Int, exercise: ExerciseRequest) {
        viewModelScope.launch {
            adminRepository.updateExercise(exerciseId, exercise)
            loadExercises(splitId)
        }
    }

    fun deleteExercise(exerciseId: Int) {
        viewModelScope.launch {
            adminRepository.deleteExercise(exerciseId)
            loadExercises(splitId)
        }
    }

    // Meals
    private val _meals = MutableStateFlow<ResultWrapper<List<MealsResponse>>>(ResultWrapper.Initial)
    val meals = _meals.asStateFlow()

    fun loadMeals() {
        viewModelScope.launch {
            _meals.value = ResultWrapper.Loading
            _meals.value = adminRepository.getMeals()
        }
    }

    fun addMeal(meal: MealRequest) {
        viewModelScope.launch {
            adminRepository.addMeal(meal)
            loadMeals()
        }
    }

    fun updateMeal(mealId: Int, meal: MealRequest) {
        viewModelScope.launch {
            adminRepository.updateMeal(mealId, meal)
            loadMeals()
        }
    }

    fun deleteMeal(mealId: Int) {
        viewModelScope.launch {
            adminRepository.deleteMeal(mealId)
            loadMeals()
        }
    }

    // Trainers
    private val _trainers = MutableStateFlow<ResultWrapper<List<TrainerResponse>>>(ResultWrapper.Initial)
    val trainers = _trainers.asStateFlow()

    fun loadTrainers() {
        viewModelScope.launch {
            _trainers.value = ResultWrapper.Loading
            _trainers.value = adminRepository.getTrainers()
        }
    }

    fun addTrainer(user: CreateUserRequest) {
        viewModelScope.launch {
            adminRepository.addUser(user)
            loadTrainers()
        }
    }

    fun updateTrainer(trainerId: Int, trainer: UpdateTrainerRequest) {
        viewModelScope.launch {
            adminRepository.updateTrainer(trainerId, trainer)
            loadTrainers()
        }
    }
    fun deleteTrainer(trainerId: Int) {
        viewModelScope.launch {
            adminRepository.deleteTrainer(trainerId)
            loadTrainers()
        }
    }

    private suspend fun getImageUrlFromBlob(imageUri: Uri?): String? = withContext(Dispatchers.IO) {
        if (imageUri == null) return@withContext null
        return@withContext try {
            val result = appWriteInteractor.uploadProfileImage(imageUri)
            Log.d(TAG, "Image upload result: $result")
            if (result.isSuccess) {
                result.getOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}