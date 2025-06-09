package com.example.myworkoutplan.data.local.workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Upsert
    suspend fun upsertWorkout(workoutPlan: WorkoutPlan): Long

    @Delete
    suspend fun deleteWorkout(workoutPlan: WorkoutPlan)

    @Query("SELECT * FROM workout_plans WHERE exercise_name = :exerciseName")
    suspend fun getWorkoutByName(exerciseName: String): WorkoutPlan?

    @Query("DELETE FROM workout_plans WHERE exercise_name = :exerciseName")
    suspend fun deleteByExerciseName(exerciseName: String)

    @Query("DELETE FROM workout_plans")
    suspend fun deleteAllWorkouts()

    @Transaction
    @Query("""
    SELECT DISTINCT wp.*
    FROM workout_plans AS wp
    INNER JOIN WorkoutMuscleCrossRef AS wmcr ON wp.id = wmcr.workoutPlanId
    INNER JOIN muscle_groups AS m ON wmcr.muscleId = m.id
    WHERE m.muscleName IN (:muscleGroups)
""")
    fun getWorkoutsByMuscleGroup(muscleGroups: List<String>): Flow<List<WorkoutWithMuscles>>

    @Query("DELETE FROM muscle_groups")
    suspend fun deleteAllMuscleGroups()

    @Upsert
    suspend fun upsertMuscleGroup(muscleGroup: MuscleGroup): Long

    @Upsert
    suspend fun upsertWorkoutMuscleCrossRef(ref: WorkoutMuscleCrossRef): Long

    @Query("DELETE FROM workout_plans WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: Int)
}