package com.example.myfitcompanion.model

enum class UserRole(val ID: String) {
    ADMIN("ADMIN"),
    TRAINER("COACH"),
    USER("USER"),
    ;
    companion object {
        fun fromID(roleId: String?) = entries.find { role -> role.ID == roleId } ?: USER
    }
}