package com.example.myworkoutplan.data.local.workoutweek

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkoutWeek(
    @PrimaryKey
    val dayOfWeek: Int, // 1=Monday, 7=Sunday (use java.time.DayOfWeek.ordinal + 1)
    val workoutType: String
)
