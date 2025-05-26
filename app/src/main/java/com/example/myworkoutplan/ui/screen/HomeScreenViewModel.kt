package com.example.myworkoutplan.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeScreenViewModel: ViewModel() {
    var visible by mutableStateOf(false)
        private set

    fun show() {
        visible = true
    }
}