package com.example.myworkoutplan.data.local.workoutweek

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WorkoutWeek::class],
    version = 1
)
abstract class WorkoutWeekDatabase:RoomDatabase() {
    abstract fun WorkoutWeekDao(): WorkoutWeekDao
    companion object {
        @Volatile
        private var INSTANCE: WorkoutWeekDatabase? = null

        fun getInstance(context: Context): WorkoutWeekDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutWeekDatabase::class.java,
                    "workoutWeek.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}