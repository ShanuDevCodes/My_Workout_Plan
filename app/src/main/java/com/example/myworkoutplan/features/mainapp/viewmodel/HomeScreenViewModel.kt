package com.example.myworkoutplan.features.mainapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myworkoutplan.core.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class HomeScreenViewModel(dataStoreManager: DataStoreManager): ViewModel() {
    val greeting: String = getGreetingForTime()
    private val _dayOfWeek = MutableStateFlow(LocalDate.now().dayOfWeek.value)
    val dayOfWeek: StateFlow<Int> = _dayOfWeek.asStateFlow()
    val workoutSplitFlow = dataStoreManager.workoutSplitFlow
    val workoutDayFlow = dataStoreManager.workoutDayFlow

    var visible by mutableStateOf(false)
        private set

    fun show() {
        visible = true
    }
    private fun getGreetingForTime(): String {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            in 18..22 -> "Good evening"
            else -> "Hello"
        }
    }
}