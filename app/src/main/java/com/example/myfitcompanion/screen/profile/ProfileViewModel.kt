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
            try {
                val response = userRepository.updateUser(request)
                if (response.isSuccessful) {
                    val updatedUser = response.body()
                    if(updatedUser != null) {
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

                } else {
                    _updateState.value = ResultWrapper.Error("Failed to update user")
                }
            } catch (e: Exception) {
                _updateState.value = ResultWrapper.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}