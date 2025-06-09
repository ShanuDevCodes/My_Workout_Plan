package com.example.myworkoutplan.features.mainapp.data

import com.example.myworkoutplan.R

val pushWorkout = listOf(
    Triple("Bench Press", R.drawable.bench_press, listOf("Chest", "Triceps", "Shoulders")),
    Triple("Shoulder Press", R.drawable.shoulder_press, listOf("Shoulders", "Triceps")),
    Triple("Inclined Bench Press", R.drawable.incline_bench_press, listOf("Chest", "Shoulders")),
    Triple("Flyes", R.drawable.shoulder_flyes, listOf("Chest")),
    Triple("Pec Deck", R.drawable.pec_deck, listOf("Chest")),
    Triple("Tricep Push Down", R.drawable.tricep_push_down, listOf("Triceps")),
    Triple("Tricep Extension", R.drawable.tricep_extension, listOf("Triceps"))
)

val pullWorkout = listOf(
    Triple("Pull Up", R.drawable.pull_up, listOf("Back", "Biceps")),
    Triple("Lat Pulldown", R.drawable.lat_pulldown, listOf("Back", "Biceps")),
    Triple("Seated Cable Row", R.drawable.seated_cable_row, listOf("Back", "Biceps")),
    Triple("T-Bar Row", R.drawable.t_bar_row, listOf("Back")),
    Triple("Dumbbell Curl", R.drawable.bicep_curl, listOf("Biceps")),
    Triple("Seated Dumbbell Curl", R.drawable.seated_bicep_curl, listOf("Biceps")),
    Triple("Concentration Curl", R.drawable.concentration_curls, listOf("Biceps"))
)

val legWorkout = listOf(
    Triple("Squats", R.drawable.squats, listOf("Quads", "Glutes", "Hamstrings")),
    Triple("Leg Extension", R.drawable.leg_extension, listOf("Quads")),
    Triple("Leg Curl", R.drawable.leg_curls, listOf("Hamstrings")),
    Triple("Close leg Press", R.drawable.close_leg_press, listOf("Quads", "Glutes")),
    Triple("Wide leg Press", R.drawable.wide_leg_press, listOf("Quads", "Glutes")),
    Triple("Calf Raise", R.drawable.calf_raises, listOf("Calves"))
)

val allMuscleGroups = listOf(
    "Chest",
    "Triceps",
    "Biceps",
    "Shoulders",
    "Back",
    "Lats",
    "Traps",
    "Forearms",
    "Core",
    "Abs",
    "Obliques",
    "Lower Back",
    "Quads",
    "Hamstrings",
    "Glutes",
    "Calves",
    "Hip Flexors",
    "Adductors",
    "Abductors",
    "Neck"
)