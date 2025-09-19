package com.example.myfitcompanion.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDao: UserDao
): ViewModel() {

    private val _updateState = MutableStateFlow<ResultWrapper<UpdateProfileResponse>>(ResultWrapper.Initial)
    val updateState: StateFlow<ResultWrapper<UpdateProfileResponse>> = _updateState

    fun updateUserInfo(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateState.value = ResultWrapper.Loading
            when (val result = userRepository.updateUser(request)) {
                is ResultWrapper.Success -> {
                    val updatedUser = result.data
                    val currentUser = userDao.getUser().firstOrNull()
                    val mergedUser: User? = currentUser?.copy(
                        name = updatedUser.name,
                        email = updatedUser.email,
                        height = updatedUser.height,
                        weight = updatedUser.weight,
                        bodyFat = updatedUser.bodyFat,
                        goal = updatedUser.goal
                    )
                    if (mergedUser != null) {
                        userDao.updateUserDetails(mergedUser)
                    }
                    _updateState.value = ResultWrapper.Success(updatedUser)
                }
                is ResultWrapper.Error -> {
                    _updateState.value = ResultWrapper.Error(result.message ?: "Failed to update user")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _updateState.value = ResultWrapper.Initial
                }
            }
        }
    }
}