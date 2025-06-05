package com.example.myworkoutplan.data.local.workoutweek

data class WorkoutWeekState(
    val dayOfWeek: Int = 0,
    val workoutType: String = "",
    val isSwapping: Boolean = false,
    val currentWorkoutDay: WorkoutWeek? = null,
    val availableSwapDays: List<WorkoutWeek> = emptyList(),
)