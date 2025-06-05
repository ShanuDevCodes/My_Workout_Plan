package com.example.myworkoutplan.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmailVerificationViewModel : ViewModel() {
    private val _secondsLeft = MutableStateFlow(30)
    val secondsLeft = _secondsLeft.asStateFlow()

    private val _resendEnabled = MutableStateFlow(false)
    val resendEnabled = _resendEnabled.asStateFlow()

    init {
        startTimer()
    }

    fun startTimer() {
        _resendEnabled.value = false
        _secondsLeft.value = 30
        viewModelScope.launch {
            while (_secondsLeft.value > 0) {
                delay(1000)
                _secondsLeft.value--
            }
            _resendEnabled.value = true
        }
    }

    fun resendEmail() {
        startTimer() // Restart timer
    }
}