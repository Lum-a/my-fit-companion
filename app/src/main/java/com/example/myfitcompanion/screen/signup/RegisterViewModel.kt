package com.example.myfitcompanion.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
): ViewModel() {

    private val _registerState = MutableStateFlow<ResultWrapper<User>>(ResultWrapper.Initial)
    val registerState: StateFlow<ResultWrapper<User>> = _registerState

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = ResultWrapper.Loading
            try {

                val response = userRepository.register(registerRequest)
                if(response.isSuccessful) {
                    val body = response.body()
                    if(body != null) {
                        tokenManager.saveToken(body.token)
                        userRepository.insertUser(body.user)
                        _registerState.value = ResultWrapper.Success(body.user)
                    } else {
                        _registerState.value = ResultWrapper.Error("Empty response")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Registration failed"
                    _registerState.value = ResultWrapper.Error(errorMsg)
                }

            } catch (e: Exception) {
                _registerState.value = ResultWrapper.Error(e.message ?: "Unknown error")
            }
        }
    }

}