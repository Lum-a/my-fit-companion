package com.example.myfitcompanion.screen.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDao: UserDao
): ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _updateState = MutableStateFlow<ResultWrapper<UpdateProfileResponse>>(ResultWrapper.Initial)
    val updateState: StateFlow<ResultWrapper<UpdateProfileResponse>> = _updateState

    init {
        viewModelScope.launch {
            val currentUser = userDao.getUser().firstOrNull()
            _user.value = currentUser
        }
    }

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
                        goal = updatedUser.goal,
                        photoUrl = updatedUser.photoUrl
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

    fun updatePhotoUrl(newUrl: String) {
        _user.update { it?.copy(photoUrl = newUrl) }
    }

    fun uploadProfilePhoto(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val storageRef = FirebaseStorage.getInstance().reference
                val userId = user.value?.id ?: "default_user"
                val photoRef = storageRef.child("profile_photos/$userId.jpg")
                val uploadTask = photoRef.putFile(uri)
                uploadTask.await()
                val downloadUrl = photoRef.downloadUrl.await().toString()
                // Update user profile with new photo URL
                updateUserInfo(UpdateProfileRequest(photoUrl = downloadUrl))
            } catch (e: Exception) {
                _updateState.value = ResultWrapper.Error("Photo upload failed: ${e.localizedMessage}")
            }
        }
    }
}