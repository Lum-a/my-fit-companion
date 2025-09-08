package com.example.myfitcompanion.utils

fun isValidEmail(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(gmail\\.com|outlook\\.com|yahoo\\.com)$")
    return regex.matches(email)
}

fun isValidPassword(password: String): Boolean {
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
    return regex.matches(password)
}