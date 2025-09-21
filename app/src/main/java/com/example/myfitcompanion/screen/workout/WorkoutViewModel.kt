package com.example.myfitcompanion.screen.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.WorkoutResponse
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _workoutState = MutableStateFlow<ResultWrapper<List<WorkoutResponse>>>(ResultWrapper.Initial)
    val workoutState: StateFlow<ResultWrapper<List<WorkoutResponse>>> = _workoutState.asStateFlow()

    fun loadWorkouts() {
        viewModelScope.launch {
            _workoutState.value = ResultWrapper.Loading
            when (val result = userRepository.getWorkouts()) {
                is ResultWrapper.Success -> {
                    _workoutState.value = ResultWrapper.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _workoutState.value = ResultWrapper.Error(result.message ?: "Failed to load workouts")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _workoutState.value = ResultWrapper.Error("Unexpected initial state")
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