package com.example.myworkoutplan.ui.components.workoutDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Upsert
    suspend fun upsertWorkout(workoutPlan: WorkoutPlan)

    @Delete
    suspend fun deleteWorkout(workoutPlan: WorkoutPlan)

    @Query("SELECT exercise_name as workoutName,image_resource as imageResource FROM workout_plans WHERE workout_type = :workoutType")
    fun getWorkoutsByType(workoutType: String): Flow<List<WorkoutWithImage>>

    @Query("SELECT DISTINCT workout_type as workoutType,workout_type_image as workoutTypeImage FROM workout_plans")
    fun getAllWorkoutTypes(): Flow<List<WorkoutTypeWithImage>>

    @Query("SELECT * FROM workout_plans WHERE exercise_name = :exerciseName")
    suspend fun getWorkoutByName(exerciseName: String): WorkoutPlan?

    @Query("DELETE FROM workout_plans WHERE exercise_name = :exerciseName")
    suspend fun deleteByExerciseName(exerciseName: String)

    @Query("DELETE FROM workout_plans")
    suspend fun deleteAllWorkouts()
}