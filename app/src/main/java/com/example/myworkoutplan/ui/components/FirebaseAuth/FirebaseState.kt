package com.example.myworkoutplan.ui.components.FirebaseAuth

data class FirebaseState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val error: String = "",
    val isError: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false
)