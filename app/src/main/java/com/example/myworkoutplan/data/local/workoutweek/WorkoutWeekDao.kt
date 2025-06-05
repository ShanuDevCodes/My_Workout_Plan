package com.example.myworkoutplan.data.local.workoutweek

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutWeekDao {

    @Upsert
    suspend fun insertAll(workoutWeek: List<WorkoutWeek>)

    @Query("SELECT * FROM WorkoutWeek WHERE dayOfWeek = :dayOfWeek")
    suspend fun getWorkoutWeek(dayOfWeek: Int): WorkoutWeek?

    @Query("SELECT * FROM WorkoutWeek")
    fun getAllWorkoutDays(): Flow<List<WorkoutWeek>>

    @Query("DELETE FROM WorkoutWeek")
    suspend fun deleteAll()

    @Update
    suspend fun update(workoutWeek: WorkoutWeek)

}