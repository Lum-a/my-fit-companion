package com.example.myfitcompanion.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.model.User
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
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

    private val _loginState = MutableStateFlow<ResultWrapper<User>>(ResultWrapper.Initial)
    val loginState: StateFlow<ResultWrapper<User>> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ResultWrapper.Loading
            try {
                val response  = userRepository.login(email, password)
                if(response.isSuccessful) {
                    val loginData = response.body()
                    if(loginData != null) {
                        tokenManager.saveToken(loginData.token)
                        userRepository.insertUser(loginData.user)
                        _loginState.value = ResultWrapper.Success(loginData.user)
                    } else {
                        _loginState.value = ResultWrapper.Error("Empty response body")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login failed"
                    _loginState.value = ResultWrapper.Error(errorMsg)
                }
            } catch (e: Exception) {
                _loginState.value = ResultWrapper.Error(e.message ?: "Unknown error")
            }
        }
    }
}