package com.example.myworkoutplan.data.remote.firebaseauth

data class FirebaseState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val error: String = "",
    val isError: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false
)