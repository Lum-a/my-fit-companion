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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appWriteInteractor: AppWriteInteractor
): ViewModel() {

    val user = userRepository.getUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    private val _updateState = MutableStateFlow<ResultWrapper<UpdateProfileResponse>>(ResultWrapper.Initial)
    val updateState: StateFlow<ResultWrapper<UpdateProfileResponse>> = _updateState

    fun updateProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateState.value = ResultWrapper.Loading
            val response = userRepository.updateUser(request)
            _updateState.value = response
        }
    }


    // Separate state for image upload
    private val _imageUploadState = MutableStateFlow<ResultWrapper<String>>(ResultWrapper.Initial)
    val imageUploadState: StateFlow<ResultWrapper<String>> = _imageUploadState

    fun uploadProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            _imageUploadState.value = ResultWrapper.Loading
            try {
                val result = appWriteInteractor.uploadProfileImage(imageUri)
                Log.d("ProfileViewModel", "Image upload result: $result")
                if (result.isSuccess) {
                    val appWriteUrl = result.getOrNull()
                    Log.d("ProfileViewModel", "AppWrite URL: $appWriteUrl")
                    if(appWriteUrl != null) {
                        _imageUploadState.value = ResultWrapper.Success(appWriteUrl)
                    } else {
                        _imageUploadState.value = ResultWrapper.Error("Failed to retrieve image URL")
                    }
                } else {
                    _imageUploadState.value = ResultWrapper.Error("Failed to upload image")
                }
            } catch (e: Exception) {
                _imageUploadState.value = ResultWrapper.Error(e.message ?: "Upload failed")
            }
        }
    }
}