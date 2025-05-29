package com.example.myworkoutplan.ui.components.workoutDB

sealed interface WorkoutEvent {
    object SaveWorkout : WorkoutEvent
    data class SetExerciseName(val exerciseName: String) : WorkoutEvent
    data class SetImageResource(val imageResource: Int) : WorkoutEvent
    data class SetWorkoutType(val workoutType: String) : WorkoutEvent
    data class SetWorkoutTypeImage(val workoutTypeImage: Int) : WorkoutEvent
    object ShowDialog : WorkoutEvent
    object HideDialog : WorkoutEvent
    data class DeleteWorkout(val workoutName: String) : WorkoutEvent
    object ResetWorkoutDB: WorkoutEvent
}