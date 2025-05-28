package com.example.myworkoutplan.ui.plans_navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PlansScreenViewModel: ViewModel() {
    var visible by mutableStateOf(false)

    fun show() {
        visible = true
    }
}