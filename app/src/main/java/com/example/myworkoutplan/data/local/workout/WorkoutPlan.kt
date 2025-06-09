package com.example.myworkoutplan.data.local.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_plans"
)
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

@Entity(
    tableName = "muscle_groups",
)
data class MuscleGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val muscleName: String,
)

@Entity(
    primaryKeys = ["workoutPlanId", "muscleId"],
    indices = [Index("muscleId")],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MuscleGroup::class,
            parentColumns = ["id"],
            childColumns = ["muscleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutMuscleCrossRef(
    val workoutPlanId: Int,
    val muscleId: Int
)