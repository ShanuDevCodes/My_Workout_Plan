package com.example.myworkoutplan.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myworkoutplan.data.local.workout.WorkoutDao
import com.example.myworkoutplan.data.local.workout.WorkoutPlan
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeek
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekDao

@Database(
    entities = [WorkoutPlan::class, WorkoutWeek::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WorkoutWeekDao(): WorkoutWeekDao
    abstract fun workoutDao(): WorkoutDao
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