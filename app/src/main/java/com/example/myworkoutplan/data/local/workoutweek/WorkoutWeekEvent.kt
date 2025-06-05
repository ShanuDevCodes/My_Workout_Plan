package com.example.myworkoutplan.data.local.workoutweek

sealed interface WorkoutWeekEvent {
    data class SetDayOfWeek(val dayOfWeek: Int) : WorkoutWeekEvent
    data class SetWorkoutType(val workoutType: String) : WorkoutWeekEvent
    data class SwapWorkoutWeek(val weekDay:Int) : WorkoutWeekEvent
    data object ShowSwapDialog : WorkoutWeekEvent
    data object HideSwapDialog : WorkoutWeekEvent
}