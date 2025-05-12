package com.example.myworkoutplan.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import java.time.DayOfWeek
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(){
    val dayOfWeek = LocalDate.now().dayOfWeek
    val (title, workout) = when (dayOfWeek) {
        DayOfWeek.MONDAY, DayOfWeek.THURSDAY -> "Push Day" to pushWorkout
        DayOfWeek.TUESDAY, DayOfWeek.FRIDAY -> "Pull Day" to pullWorkout
        DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY -> "Leg Day" to legWorkout
        else -> "Rest Day" to emptyList()
    }

    DayScreen(title,workout)
}