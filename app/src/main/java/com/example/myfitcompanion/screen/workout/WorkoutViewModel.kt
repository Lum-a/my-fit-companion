package com.example.myfitcompanion.screen.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.SplitResponse
import com.example.myfitcompanion.api.model.WorkoutResponse
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _workoutState = MutableStateFlow<ResultWrapper<List<WorkoutResponse>>>(ResultWrapper.Initial)
    val workoutState = _workoutState.asStateFlow()

    private val _splitState = MutableStateFlow<ResultWrapper<List<SplitResponse>>>(ResultWrapper.Initial)
    val splitState = _splitState.asStateFlow()

    private val _exerciseState = MutableStateFlow<ResultWrapper<List<ExerciseResponse>>>(ResultWrapper.Initial)
    val exerciseState = _exerciseState.asStateFlow()

    fun loadWorkouts() {
        viewModelScope.launch {
            _workoutState.value = ResultWrapper.Loading
            when (val result = userRepository.getWorkouts()) {
                is ResultWrapper.Initial -> {
                    _workoutState.value = ResultWrapper.Initial
                }
                is ResultWrapper.Success -> {
                    _workoutState.value = ResultWrapper.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _workoutState.value = ResultWrapper.Error(result.message ?: "Failed to load workouts")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }

    fun loadSplits(workoutId: Int) {
        viewModelScope.launch {
            _splitState.value = ResultWrapper.Loading
            when (val result = userRepository.getSplits(workoutId)) {
                is ResultWrapper.Success -> {
                    _splitState.value = ResultWrapper.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _splitState.value = ResultWrapper.Error(result.message ?: "Failed to load splits")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _splitState.value = ResultWrapper.Error("Unexpected initial state")
                }
            }
        }
    }

    fun loadExercises(splitId: Int) {
        viewModelScope.launch {
            _exerciseState.value = ResultWrapper.Loading
            when (val result = userRepository.getExercises(splitId)) {
                is ResultWrapper.Success -> {
                    _exerciseState.value = ResultWrapper.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _exerciseState.value = ResultWrapper.Error(result.message ?: "Failed to load exercises")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _exerciseState.value = ResultWrapper.Error("Unexpected initial state")
                }
            }
        }
    }

    fun addRecentExercise(workoutId: Int) {
        viewModelScope.launch {
            userRepository.addRecentExercise(workoutId)
        }
    }
}