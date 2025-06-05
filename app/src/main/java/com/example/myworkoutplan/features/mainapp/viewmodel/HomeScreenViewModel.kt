package com.example.myworkoutplan.features.mainapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class HomeScreenViewModel: ViewModel() {
    val greeting: String = getGreetingForTime()
    val dayOfWeek: DayOfWeek? = LocalDate.now().dayOfWeek
    val title = when (dayOfWeek) {
        DayOfWeek.MONDAY, DayOfWeek.THURSDAY -> "Push Day"
        DayOfWeek.TUESDAY, DayOfWeek.FRIDAY -> "Pull Day"
        DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY -> "Leg Day"
        else -> "Rest Day"
    }
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