package com.example.myfitcompanion.admin.viewmodel

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
import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.api.model.UpdateTrainerRequest
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
): ViewModel() {
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

    fun addWorkout(workout: WorkoutRequest) {
        viewModelScope.launch {
            adminRepository.addWorkout(workout)
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

    // Exercises
    private val _exercises = MutableStateFlow<ResultWrapper<List<ExerciseResponse>>>(ResultWrapper.Initial)
    val exercises = _exercises.asStateFlow()

    fun loadExercises() {
        viewModelScope.launch {
            _exercises.value = ResultWrapper.Loading
            _exercises.value = adminRepository.getExercises()
        }
    }

    fun addExercise(exercise: ExerciseRequest) {
        viewModelScope.launch {
            adminRepository.addExercise(exercise)
            loadExercises()
        }
    }

    fun updateExercise(exerciseId: Int, exercise: ExerciseRequest) {
        viewModelScope.launch {
            adminRepository.updateExercise(exerciseId, exercise)
            loadExercises()
        }
    }

    fun deleteExercise(exerciseId: Int) {
        viewModelScope.launch {
            adminRepository.deleteExercise(exerciseId)
            loadExercises()
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
}