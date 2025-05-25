package com.example.myworkoutplan.ui.components.workoutDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WorkoutPlan::class],
    version = 1
)
abstract class WorkoutDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}