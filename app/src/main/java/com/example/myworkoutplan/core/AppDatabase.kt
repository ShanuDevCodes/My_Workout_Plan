package com.example.myworkoutplan.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myworkoutplan.data.local.workout.MuscleGroup
import com.example.myworkoutplan.data.local.workout.SplitDay
import com.example.myworkoutplan.data.local.workout.SplitDayWeekDayCrossRef
import com.example.myworkoutplan.data.local.workout.SplitDayWorkoutCrossRef
import com.example.myworkoutplan.data.local.workout.WeekDay
import com.example.myworkoutplan.data.local.workout.WorkoutDao
import com.example.myworkoutplan.data.local.workout.WorkoutMuscleCrossRef
import com.example.myworkoutplan.data.local.workout.WorkoutPlan
import com.example.myworkoutplan.data.local.workout.WorkoutSplit

@Database(
    entities = [WorkoutPlan::class, MuscleGroup::class, WorkoutMuscleCrossRef::class, WorkoutSplit::class, SplitDay::class, SplitDayWorkoutCrossRef::class, WeekDay::class, SplitDayWeekDayCrossRef::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WorkoutDao(): WorkoutDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE workout_plans ADD COLUMN is_body_weight INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "workout_app.db"
                )
                    .addMigrations(MIGRATION_3_4)
                    .build().also { INSTANCE = it }
            }
        }
    }
}