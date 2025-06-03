package com.example.myworkoutplan.features.mainapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.DayOfWeek
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HomeScreenViewModel: ViewModel() {
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
}