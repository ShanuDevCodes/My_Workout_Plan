package com.example.myworkoutplan.data.local.workout

data class WorkoutState(
    val workoutObjectByType: List<WorkoutPlan> = emptyList(),
    val exerciseName: String = "",
    val imageResource: Int = 0,
    val workoutType: String = "",
    val workoutTypeImage: Int = 0,
    val isAddingWorkout: Boolean = false,
    val nameAlreadyExists: Boolean = false,
    val workoutWithMuscleGroups: List<WorkoutWithMuscles> = emptyList()
)
