package com.example.myworkoutplan.ui.components.workoutDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_plans")
data class WorkoutPlan(
    @ColumnInfo(name = "exercise_name")
    val exerciseName: String,

    @ColumnInfo(name = "image_resource")
    val imageResource: Int,

    @ColumnInfo(name = "workout_type")
    val workoutType: String,

    @ColumnInfo(name = "workout_type_image")
    val workoutTypeImage: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

