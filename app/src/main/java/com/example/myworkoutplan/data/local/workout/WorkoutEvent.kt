package com.example.myworkoutplan.data.local.workout

sealed interface WorkoutEvent {
    data object SaveWorkout : WorkoutEvent
    data class SetExerciseName(val exerciseName: String) : WorkoutEvent
    data class SetImageResource(val imageResource: Int) : WorkoutEvent
    data class SetWorkoutType(val workoutType: String) : WorkoutEvent
    data class SetWorkoutTypeImage(val workoutTypeImage: Int) : WorkoutEvent
    data object ShowDialog : WorkoutEvent
    data object HideDialog : WorkoutEvent
    data class DeleteWorkout(val workoutPlan: WorkoutPlan) : WorkoutEvent
    data class DeleteWorkoutByName(val workoutName: String) : WorkoutEvent
    data object ResetWorkoutDB: WorkoutEvent
    data class GetWorkoutsByMuscleGroup(val muscleGroups: List<String>) : WorkoutEvent
    data class DeleteWorkoutByMuscleGroup(val workoutWithMuscles: WorkoutWithMuscles) : WorkoutEvent
    data object GetAllWorkoutSplits : WorkoutEvent
    data class GetAllSplitDaysForWorkoutSplit(val splitId: Int) : WorkoutEvent
    data class GetWorkoutBySplitDay(val splitDayId: Int) : WorkoutEvent
    data object GetAllSplitDays : WorkoutEvent
    data class GetSplitDay(val splitDayId: Int) : WorkoutEvent
    data object GetAllWorkouts : WorkoutEvent
    data class DeleteWorkoutFromSplitDay(val splitDayId: Int, val workoutId: Int) : WorkoutEvent
    data class GetSplit(val splitId: Int) : WorkoutEvent
    data class UpsertWorkoutInSplitCrossRef(val splitDayId: Int, val workoutId: Int) : WorkoutEvent
    data class GetWorkoutPlansForSplitAndWeekDay(val splitName: String, val weekDayID: Int) : WorkoutEvent
    data class GetSplitDayForSplitAndWeekDay(val splitName: String, val weekDayID: Int) : WorkoutEvent
}