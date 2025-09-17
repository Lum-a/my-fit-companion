package com.example.myfitcompanion.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.admin.repository.AdminRepository
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository
): ViewModel() {

    private val _users = MutableStateFlow<ResultWrapper<List<UserResponse>>>(ResultWrapper.Initial)
    val users: StateFlow<ResultWrapper<List<UserResponse>>> = _users

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = ResultWrapper.Loading
            _users.value = adminRepository.getUsers()
        }
    }

    fun addUser(user: UserResponse) {
        viewModelScope.launch {
            adminRepository.addUser(user)
            loadUsers() // refresh list
        }
    }

    fun updateUser(user: UserResponse) {
        viewModelScope.launch {
            adminRepository.updateUser(user)
            loadUsers() // refresh list
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            adminRepository.deleteUser(userId)
            loadUsers()
        }
    }
}