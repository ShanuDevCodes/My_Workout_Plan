package com.example.myworkoutplan.features.mainapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.core.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class HomeScreenViewModel(private val dataStoreManager: DataStoreManager): ViewModel() {
    val greeting: String = getGreetingForTime()
    val workoutSplitFlow = dataStoreManager.workoutSplitFlow
    val workoutDayFlow = dataStoreManager.workoutDayFlow
    private val _dialogVisible = MutableStateFlow(false)
    val dialogVisible: StateFlow<Boolean> = _dialogVisible.asStateFlow()
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
    fun showDialog(){
        _dialogVisible.value = true
    }
    fun hideDialog(){
        _dialogVisible.value = false
    }

    fun setWorkoutDay(workoutDay: String) {
        viewModelScope.launch {
            dataStoreManager.setWorkoutDay(workoutDay)
        }
    }
}