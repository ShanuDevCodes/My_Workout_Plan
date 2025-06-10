package com.example.myworkoutplan.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myworkoutplan.data.local.workout.MuscleGroup
import com.example.myworkoutplan.data.local.workout.SplitDay
import com.example.myworkoutplan.data.local.workout.SplitDayWorkoutCrossRef
import com.example.myworkoutplan.data.local.workout.WorkoutDao
import com.example.myworkoutplan.data.local.workout.WorkoutMuscleCrossRef
import com.example.myworkoutplan.data.local.workout.WorkoutPlan
import com.example.myworkoutplan.data.local.workout.WorkoutSplit
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeek
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekDao

@Database(
    entities = [WorkoutPlan::class, WorkoutWeek::class, MuscleGroup::class, WorkoutMuscleCrossRef::class, WorkoutSplit::class, SplitDay::class, SplitDayWorkoutCrossRef::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WorkoutWeekDao(): WorkoutWeekDao
    abstract fun WorkoutDao(): WorkoutDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "workout_app.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}