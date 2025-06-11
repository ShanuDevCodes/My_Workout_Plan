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

@Entity(tableName = "workout_splits")
data class WorkoutSplit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val splitName: String
)

@Entity(
    tableName = "split_days",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSplit::class,
            parentColumns = ["id"],
            childColumns = ["splitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("splitId")]
)
data class SplitDay(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val splitDayName: String,
    val splitDayImage: Int,
    val splitId: Int
)

@Entity(
    primaryKeys = ["splitDayId", "workoutPlanId"],
    foreignKeys = [
        ForeignKey(
            entity = SplitDay::class,
            parentColumns = ["id"],
            childColumns = ["splitDayId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutPlanId")]
)
data class SplitDayWorkoutCrossRef(
    val splitDayId: Int,
    val workoutPlanId: Int
)

@Entity(tableName = "week_days")
data class WeekDay(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayName: String // e.g., "Monday"
)

@Entity(
    primaryKeys = ["splitDayId", "weekDayId"],
    foreignKeys = [
        ForeignKey(
            entity = SplitDay::class,
            parentColumns = ["id"],
            childColumns = ["splitDayId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WeekDay::class,
            parentColumns = ["id"],
            childColumns = ["weekDayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("weekDayId")]
)
data class SplitDayWeekDayCrossRef(
    val splitDayId: Int,
    val weekDayId: Int
)