package com.example.myworkoutplan.data.local.workout

sealed interface WorkoutEvent {
    object SaveWorkout : WorkoutEvent
    data class SetExerciseName(val exerciseName: String) : WorkoutEvent
    data class SetImageResource(val imageResource: Int) : WorkoutEvent
    data class SetWorkoutType(val workoutType: String) : WorkoutEvent
    data class SetWorkoutTypeImage(val workoutTypeImage: Int) : WorkoutEvent
    object ShowDialog : WorkoutEvent
    object HideDialog : WorkoutEvent
    data class DeleteWorkout(val workoutPlan: WorkoutPlan) : WorkoutEvent
    data class DeleteWorkoutByName(val workoutName: String) : WorkoutEvent
    object ResetWorkoutDB: WorkoutEvent
    data class GetWorkoutObjectByType(val workoutType: String) : WorkoutEvent
}