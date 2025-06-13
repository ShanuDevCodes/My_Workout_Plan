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

    @Query("DELETE FROM SplitDayWorkoutCrossRef")
    suspend fun deleteAllSplitDayWorkoutCrossRefs()

    @Query("DELETE FROM split_days")
    suspend fun deleteAllSplitDays()

    @Query("DELETE FROM workout_splits")
    suspend fun deleteAllWorkoutSplits()

    @Query("DELETE FROM workoutmusclecrossref")
    suspend fun deleteAllWorkoutMuscleCrossRefs()

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

    @Upsert
    suspend fun upsertWorkoutSplit(split: WorkoutSplit): Long

    @Upsert
    suspend fun upsertSplitDay(splitDay: SplitDay): Long

    @Upsert
    suspend fun upsertSplitDayWorkoutCrossRef(crossRef: SplitDayWorkoutCrossRef)

    @Query("SELECT * FROM workout_splits")
    fun getAllWorkoutSplits(): Flow<List<WorkoutSplit>>

    @Query("SELECT * FROM split_days")
    fun getAllSplitDays(): Flow<List<SplitDay>>

    @Query("SELECT * FROM split_days WHERE splitId = :splitId")
    fun getSplitDaysForSplit(splitId: Int): Flow<List<SplitDay>>

    @Transaction
    @Query("""
        SELECT wp.* FROM workout_plans AS wp
        INNER JOIN splitdayworkoutcrossref AS crossRef
            ON wp.id = crossRef.workoutPlanId
        WHERE crossRef.splitDayId = :splitDayId
    """)
    fun getWorkoutsBySplitDay(splitDayId: Int): Flow<List<WorkoutPlan>>

    @Query("SELECT * FROM SPLIT_DAYS WHERE id = :splitId")
    fun getSplitDaysBySplitId(splitId: Int): Flow<SplitDay?>

    @Query("SELECT * FROM workout_plans")
    fun getAllWorkouts(): Flow<List<WorkoutPlan>>

    @Query("DELETE FROM sqlite_sequence")
    suspend fun resetAllAutoIncrement()

    @Query("DELETE FROM SplitDayWorkoutCrossRef where workoutPlanId = :workoutId and splitDayId = :splitDayId")
    suspend fun deleteSplitDayWorkoutCrossRefByWorkoutId(splitDayId: Int, workoutId: Int)

    @Query("SELECT * FROM workout_splits WHERE id = :splitId")
    fun getWorkoutSplitsBySplitId(splitId: Int): Flow<WorkoutSplit?>

    @Query("SELECT * From workout_splits WHERE splitName = :splitName")
    fun getWorkoutSplitByName(splitName: String): Flow<WorkoutSplit?>

    @Query("SELECT * FROM splitdayworkoutcrossref where splitDayId = :splitDayId and workoutPlanId = :workoutId")
    suspend fun getSplitDayWorkoutCrossRef(splitDayId: Int, workoutId: Int): SplitDayWorkoutCrossRef?

    @Query("DELETE FROM week_days")
    suspend fun deleteAllWeekDays()

    @Upsert
    suspend fun upsertWeekDay(weekDay: WeekDay): Long

    @Upsert
    suspend fun upsertSplitDayWeekDayCrossRef(crossRef: SplitDayWeekDayCrossRef): Long

    @Query("""
    SELECT wp.* FROM workout_plans AS wp
    INNER JOIN splitdayworkoutcrossref AS sdwcr ON wp.id = sdwcr.workoutPlanId
    INNER JOIN split_days AS sd ON sdwcr.splitDayId = sd.id
    INNER JOIN workout_splits AS ws ON sd.splitId = ws.id
    INNER JOIN splitdayweekdaycrossref AS sdwdcr ON sd.id = sdwdcr.splitDayId
    WHERE ws.splitName = :splitName AND sdwdcr.weekDayId = :weekDayId
""")
    fun getWorkoutPlansForSplitNameAndWeekDay(splitName: String, weekDayId: Int): Flow<List<WorkoutPlan>>

    @Query("""
    SELECT * FROM split_days AS sd
    INNER JOIN workout_splits AS ws ON sd.splitId = ws.id
    INNER JOIN splitdayweekdaycrossref AS sdwdcr ON sd.id = sdwdcr.splitDayId
    WHERE ws.splitName = :splitName AND sdwdcr.weekDayId = :weekDayId
""")
    fun getSplitDayForSplitAndWeekDay(splitName: String, weekDayId: Int): Flow<SplitDay?>

    @Query("""
    SELECT sd.* FROM split_days AS sd
    INNER JOIN workout_splits AS ws ON sd.splitId = ws.id
    WHERE ws.splitName = :splitName AND sd.splitDayName = :splitDayName
    LIMIT 1
""")
    fun getSplitDayForSplitAndDayName(
        splitName: String,
        splitDayName: String
    ): Flow<SplitDay?>

    @Query("""
    SELECT wp.* FROM workout_plans wp
    INNER JOIN splitdayworkoutcrossref sdwcr ON wp.id = sdwcr.workoutPlanId
    INNER JOIN split_days sd ON sdwcr.splitDayId = sd.id
    INNER JOIN workout_splits ws ON sd.splitId = ws.id
    WHERE ws.splitName = :splitName AND sd.splitDayName = :splitDayName
""")
    fun getWorkoutPlansForSplitAndDay(
        splitName: String,
        splitDayName: String
    ): Flow<List<WorkoutPlan>>
}