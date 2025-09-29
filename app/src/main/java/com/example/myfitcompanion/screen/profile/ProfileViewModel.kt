package com.example.myfitcompanion.screen.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.AppWriteInteractor
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appWriteInteractor: AppWriteInteractor
): ViewModel() {

    private val TAG = "ProfileViewModel"

    val user = userRepository.getUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _updateState = MutableStateFlow<ResultWrapper<UpdateProfileResponse>>(ResultWrapper.Initial)
    val updateState: StateFlow<ResultWrapper<UpdateProfileResponse>> = _updateState

    private val _changeEmailState = MutableStateFlow<ResultWrapper<String>>(ResultWrapper.Initial)
    val changeEmailState: StateFlow<ResultWrapper<String>> = _changeEmailState

    private val _changePasswordState = MutableStateFlow<ResultWrapper<String>>(ResultWrapper.Initial)
    val changePasswordState: StateFlow<ResultWrapper<String>> = _changePasswordState

    fun updateProfile(request: UpdateProfileRequest, imageUri: Uri?) {
        viewModelScope.launch {
            _updateState.value = ResultWrapper.Loading
            val updatedRequest = request.copy(imageUrl = getImageUrlFromBlob(imageUri)) // Upload image and get URL
            val response = userRepository.updateUser(updatedRequest)
            _updateState.value = response
        }
    }

    fun changeEmail(newEmail: String) {
        viewModelScope.launch {
            _changeEmailState.value = ResultWrapper.Loading
            try {
                val response = userRepository.updateEmail(newEmail)

                when (response) {
                    is ResultWrapper.Success -> {
                        val currentUser = user.value
                        if (currentUser != null) {
                            val updatedUser = currentUser.copy(email = newEmail)
                            userRepository.insertUser(updatedUser)
                        }

                        _changeEmailState.value =
                            ResultWrapper.Success("Email changed successfully!")
                    }

                    is ResultWrapper.Error -> {
                        _changeEmailState.value =
                            ResultWrapper.Error(response.message ?: "Failed to change email")
                    }

                    else -> {
                        _changeEmailState.value =
                            ResultWrapper.Error("Failed to change email")
                    }
                }
            } catch (e: Exception) {
                _changeEmailState.value =
                    ResultWrapper.Error("Failed to change email: ${e.localizedMessage}")
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = ResultWrapper.Loading
            try {
                val response = userRepository.updatePassword(oldPassword, newPassword)

                when (response) {
                    is ResultWrapper.Success -> {
                        _changePasswordState.value = ResultWrapper.Success(response.data.getDisplayMessage())
                    }
                    is ResultWrapper.Error -> {
                        _changePasswordState.value = ResultWrapper.Error(response.message ?: "Failed to change password")
                    }
                    else -> {
                        _changePasswordState.value = ResultWrapper.Error("Failed to change password")
                    }
                }
            } catch (e: Exception) {
                _changePasswordState.value = ResultWrapper.Error("Failed to change password: ${e.localizedMessage}")
            }
        }
    }

    fun resetChangeEmailState() {
        _changeEmailState.value = ResultWrapper.Initial
    }

    fun resetChangePasswordState() {
        _changePasswordState.value = ResultWrapper.Initial
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