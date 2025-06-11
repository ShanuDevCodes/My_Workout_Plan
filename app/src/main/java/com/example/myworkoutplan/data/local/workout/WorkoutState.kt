package com.example.myworkoutplan.data.local.workout

data class WorkoutState(
    val split: WorkoutSplit? = null,
    val splitDay: SplitDay? = null,
    val splitDays: List<SplitDay> = emptyList(),
    val workoutSplits: List<WorkoutSplit> = emptyList(),
    val workoutsInSplitDay: List<WorkoutPlan> = emptyList(),
    val workouts: List<WorkoutPlan> = emptyList(),
    val exerciseName: String = "",
    val imageResource: Int = 0,
    val workoutType: String = "",
    val workoutTypeImage: Int = 0,
    val isAddingWorkout: Boolean = false,
    val nameAlreadyExists: Boolean = false,
    val workoutWithMuscleGroups: List<WorkoutWithMuscles> = emptyList()
)
