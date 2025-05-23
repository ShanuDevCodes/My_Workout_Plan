package com.example.myworkoutplan.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
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
    Column {
        Text(
            text = "Home",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )
        DayScreen(title, workout)
    }
}