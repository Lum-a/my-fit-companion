package com.example.myfitcompanion.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultWrapper<UserResponse>>(ResultWrapper.Initial)
    val loginState: StateFlow<ResultWrapper<UserResponse>> = _loginState

    fun login(request: LoginRequest) {
        if (!isValidEmail(request.email) || request.password.isBlank()) {
            val message = "email or password is invalid!"
            _loginState.value = ResultWrapper.Error(message)
            Log.d("LoginViewModel", message)
            return
        }
        viewModelScope.launch {
            _loginState.value = ResultWrapper.Loading
            when (val result = userRepository.login(request)) {
                is ResultWrapper.Success -> {
                    val loginData = result.data
                    val user = loginData.user.asUser()
                    tokenManager.saveToken(loginData.token)
                    userRepository.insertUser(user)
                    _loginState.value = ResultWrapper.Success(loginData.user)
                    Log.d("LoginViewModel", "Login success: ${loginData.user}")
                }
                is ResultWrapper.Error -> {
                    _loginState.value = ResultWrapper.Error(result.message ?: "Login failed")
                    Log.d("LoginViewModel", "Login error: ${result.message}")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _loginState.value = ResultWrapper.Error("Unexpected initial state")
                }
            }
        }
    }
}