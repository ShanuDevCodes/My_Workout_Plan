package com.example.myworkoutplan.data.local.workout

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WorkoutWithMuscles(
    @Embedded val workoutPlan: WorkoutPlan,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WorkoutMuscleCrossRef::class,
            parentColumn = "workoutPlanId",
            entityColumn = "muscleId"
        )
    )
    val muscles: List<MuscleGroup>
)
