package com.example.myworkoutplan.ui.components.FirebaseAuth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseViewModel: ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _state = MutableStateFlow(FirebaseState())
    val state: StateFlow<FirebaseState> = _state
    fun onEvent(event: FirebaseEvent) {
        when (event) {

            FirebaseEvent.UpdateUserName -> {
                viewModelScope.launch {
                    try {
                        auth.currentUser?.updateProfile(
                            UserProfileChangeRequest.Builder().setDisplayName(_state.value.name)
                                .build()
                        )?.await()
                    }catch (e: Exception){
                        _state.update {
                            it.copy(
                                isError = true,
                                error = e.message.toString()
                            )
                        }
                    }
                }
            }

            FirebaseEvent.SignInAnonymously -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    try {
                        auth.signInAnonymously().await()
                        withContext(Dispatchers.Main) {
                            checkLoggedInState()
                        }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                isError = true,
                                error = e.message.toString()
                            )
                        }
                    }finally {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }

            FirebaseEvent.LoginUser -> {
                if (_state.value.email.isNotBlank() && _state.value.password.isNotBlank()) {
                    _state.update { it.copy(isLoading = true) }
                    viewModelScope.launch {
                        try {
                            auth.signInWithEmailAndPassword(
                                _state.value.email,
                                _state.value.password
                            ).await()
                            withContext(Dispatchers.Main) {
                                checkLoggedInState()
                            }
                        } catch (e: Exception) {
                            _state.update {
                                it.copy(
                                    isError = true,
                                    error = e.message.toString()
                                )
                            }
                        } finally {
                            _state.update { it.copy(isLoading = false) }
                        }
                    }
                }
            }

            FirebaseEvent.LogoutUser -> {
                if (auth.currentUser != null){
                    auth.signOut()
                    checkLoggedInState()
                    _state.update {
                        it.copy(
                            isError = true,
                            error = "Logged out successfully"
                        )
                    }
                }else{
                    _state.update {
                        it.copy(
                            isError = true,
                            error = "You are not logged in"
                        )
                    }
                }
            }

            FirebaseEvent.RegisterUser -> {
                if (_state.value.email.isNotBlank() && _state.value.password.isNotBlank()) {
                    viewModelScope.launch {
                        try {
                            auth.createUserWithEmailAndPassword(
                                _state.value.email,
                                _state.value.password
                            ).await()
                            auth.currentUser?.updateProfile(
                                UserProfileChangeRequest.Builder().setDisplayName(_state.value.name)
                                    .build()
                            )?.await()
                            withContext(Dispatchers.Main){
                                checkLoggedInState()
                            }
                            //auth.currentUser?.sendEmailVerification()
                        } catch (e: Exception) {
                            _state.update {
                                it.copy(
                                    isError = true,
                                    error = e.message.toString()
                                )
                            }
                        }
                    }
                }
            }

            is FirebaseEvent.SetUserName -> {
                _state.update {
                    it.copy(
                        name = event.userName
                    )
                }
            }

            is FirebaseEvent.SetUserEmail -> {
                _state.update {
                    it.copy(
                        email = event.userEmail
                    )
                }
            }

            is FirebaseEvent.SetUserPassword -> {
                _state.update {
                    it.copy(
                        password = event.userPassword
                    )
                }
            }

            FirebaseEvent.ResetError -> {
                _state.update {
                    it.copy(
                        isError = false,
                        error = ""
                    )
                }
            }

            FirebaseEvent.ResetState -> {
                _state.update {
                    FirebaseState()
                }
            }

            FirebaseEvent.ResetPassword -> {
                viewModelScope.launch {
                    if (_state.value.email.isBlank()) {
                        _state.update {
                            it.copy(
                                isError = true,
                                error = "Please provide a valid email address."
                            )
                        }
                        return@launch
                    }
                    try {
                        auth.sendPasswordResetEmail(_state.value.email).await()
                        _state.update {
                            it.copy(
                                isError = true,
                                error = "If an account exists for this email, a password reset link has been sent."
                            )
                        }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                isError = true,
                                error = e.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }
    fun checkLoggedInState(){
        if(auth.currentUser == null) {
            _state.update {
                it.copy(
                    isLoggedIn = false
                )
            }
        }else{
            _state.update {
                it.copy(
                    isLoggedIn = true
                )
            }
        }
    }
}