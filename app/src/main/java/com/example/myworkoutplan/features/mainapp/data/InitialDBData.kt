package com.example.myworkoutplan.features.mainapp.data

import com.example.myworkoutplan.R

data class Workout(
    val name: String,
    val image: Int,
    val muscleGroups: List<String>,
    val workoutSplit: List<Pair<String,String>>
)

//val pushWorkout = listOf(
//    Triple("Bench Press", R.drawable.bench_press, listOf("Chest", "Triceps", "Shoulders"),),
//    Triple("Shoulder Press", R.drawable.shoulder_press, listOf("Shoulders", "Triceps")),
//    Triple("Inclined Bench Press", R.drawable.incline_bench_press, listOf("Chest", "Shoulders")),
//    Triple("Flyes", R.drawable.shoulder_flyes, listOf("Chest")),
//    Triple("Pec Deck", R.drawable.pec_deck, listOf("Chest")),
//    Triple("Tricep Push Down", R.drawable.tricep_push_down, listOf("Triceps")),
//    Triple("Tricep Extension", R.drawable.tricep_extension, listOf("Triceps"))
//)
//
//val pullWorkout = listOf(
//    Triple("Pull Up", R.drawable.pull_up, listOf("Back", "Biceps")),
//    Triple("Lat Pulldown", R.drawable.lat_pulldown, listOf("Back", "Biceps")),
//    Triple("Seated Cable Row", R.drawable.seated_cable_row, listOf("Back", "Biceps")),
//    Triple("T-Bar Row", R.drawable.t_bar_row, listOf("Back")),
//    Triple("Dumbbell Curl", R.drawable.bicep_curl, listOf("Biceps")),
//    Triple("Seated Dumbbell Curl", R.drawable.seated_bicep_curl, listOf("Biceps")),
//    Triple("Concentration Curl", R.drawable.concentration_curls, listOf("Biceps"))
//)
//
//val legWorkout = listOf(
//    Triple("Squats", R.drawable.squats, listOf("Quads", "Glutes", "Hamstrings")),
//    Triple("Leg Extension", R.drawable.leg_extension, listOf("Quads")),
//    Triple("Leg Curl", R.drawable.leg_curls, listOf("Hamstrings")),
//    Triple("Close leg Press", R.drawable.close_leg_press, listOf("Quads", "Glutes")),
//    Triple("Wide leg Press", R.drawable.wide_leg_press, listOf("Quads", "Glutes")),
//    Triple("Calf Raise", R.drawable.calf_raises, listOf("Calves"))
//)

val allMuscleGroups = listOf(
    "Chest",
    "Triceps",
    "Shoulders",
    "Back",
    "Biceps",
    "Quads",
    "Glutes",
    "Hamstrings",
    "Calves"
)

val allWorkout = listOf(
    Workout("Bench Press", R.drawable.bench_press, listOf("Chest", "Triceps", "Shoulders"), listOf("Full Body Split" to "Full Body Day", "Push,Pull,Legs Split" to "Push Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Chest Day", "Arnold Split" to "Chest & Back Day")),
    Workout("Shoulder Press", R.drawable.shoulder_press, listOf("Shoulders", "Triceps"), listOf("Full Body Split" to "Full Body Day", "Push,Pull,Legs Split" to "Push Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Shoulder Day", "Arnold Split" to "Arms & Shoulders Day")),
    Workout("Inclined Bench Press", R.drawable.incline_bench_press, listOf("Chest", "Shoulders"), listOf("Push,Pull,Legs Split" to "Push Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Chest Day", "Arnold Split" to "Chest & Back Day")),
    Workout("Flyes", R.drawable.shoulder_flyes, listOf("Chest"), listOf("Bro Split" to "Chest Day", "Arnold Split" to "Arms & Shoulders Day")),
    Workout("Pec Deck", R.drawable.pec_deck, listOf("Chest"), listOf("Bro Split" to "Chest Day")),
    Workout("Tricep Push Down", R.drawable.tricep_push_down, listOf("Triceps"), listOf("Push,Pull,Legs Split" to "Push Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Arms Day", "Arnold Split" to "Arms & Shoulders Day")),
    Workout("Tricep Extension", R.drawable.tricep_extension, listOf("Triceps"), listOf("Bro Split" to "Arms Day", "Arnold Split" to "Arms & Shoulders Day")),
    Workout("Pull Up", R.drawable.pull_up, listOf("Back", "Biceps"), listOf("Full Body Split" to "Full Body Day", "Push,Pull,Legs Split" to "Pull Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Back Day", "Arnold Split" to "Chest & Back Day")),
    Workout("Lat Pulldown", R.drawable.lat_pulldown, listOf("Back", "Biceps"), listOf("Push,Pull,Legs Split" to "Pull Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Back Day", "Arnold Split" to "Chest & Back Day")),
    Workout("Seated Cable Row", R.drawable.seated_cable_row, listOf("Back", "Biceps"), listOf("Full Body Split" to "Full Body Day", "Push,Pull,Legs Split" to "Pull Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Back Day", "Arnold Split" to "Chest & Back Day")),
    Workout("T-Bar Row", R.drawable.t_bar_row, listOf("Back"), listOf("Bro Split" to "Back Day", "Arnold Split" to "Chest & Back Day")),
    Workout("Dumbbell Curl", R.drawable.bicep_curl, listOf("Biceps"), listOf("Push,Pull,Legs Split" to "Pull Day", "Upper, Lower Body Split" to "Upper Body Day", "Bro Split" to "Arms Day", "Arnold Split" to "Arms & Shoulders Day")),
    Workout("Seated Dumbbell Curl", R.drawable.seated_bicep_curl, listOf("Biceps"), listOf("Bro Split" to "Arms Day", "Arnold Split" to "Arms & Shoulders Day")),
    Workout("Concentration Curl", R.drawable.concentration_curls, listOf("Biceps"), listOf("Bro Split" to "Arms Day", "Arnold Split" to "Arms & Shoulders Day")),
    Workout("Squats", R.drawable.squats, listOf("Quads", "Glutes", "Hamstrings"), listOf("Full Body Split" to "Full Body Day", "Push,Pull,Legs Split" to "Legs Day", "Upper, Lower Body Split" to "Lower Body Day", "Bro Split" to "Legs Day", "Arnold Split" to "Legs Day")),
    Workout("Leg Extension", R.drawable.leg_extension, listOf("Quads"), listOf("Push,Pull,Legs Split" to "Legs Day", "Upper, Lower Body Split" to "Lower Body Day", "Bro Split" to "Legs Day", "Arnold Split" to "Legs Day")),
    Workout("Leg Curl", R.drawable.leg_curls, listOf("Hamstrings"), listOf("Push,Pull,Legs Split" to "Legs Day", "Upper, Lower Body Split" to "Lower Body Day", "Bro Split" to "Legs Day", "Arnold Split" to "Legs Day")),
    Workout("Close leg Press", R.drawable.close_leg_press, listOf("Quads", "Glutes"), listOf("Bro Split" to "Legs Day", "Arnold Split" to "Legs Day")),
    Workout("Wide leg Press", R.drawable.wide_leg_press, listOf("Quads", "Glutes"), listOf("Bro Split" to "Legs Day", "Arnold Split" to "Legs Day")),
    Workout("Calf Raise", R.drawable.calf_raises, listOf("Calves"), listOf("Full Body Split" to "Full Body Day", "Push,Pull,Legs Split" to "Legs Day", "Upper, Lower Body Split" to "Lower Body Day", "Bro Split" to "Legs Day", "Arnold Split" to "Legs Day"))
)


val allWorkoutSplit = listOf(
    ("Push,Pull,Legs Split" to listOf("Push Day" to R.drawable.push_day, "Pull Day" to R.drawable.pull_day, "Legs Day" to R.drawable.leg_day)),
    ("Full Body Split" to listOf("Full Body Day" to R.drawable.full_body_day)),
    ("Upper, Lower Body Split" to listOf("Upper Body Day" to R.drawable.upper_body, "Lower Body Day" to R.drawable.lower_body)),
    ("Bro Split" to listOf("Chest Day" to R.drawable.chest_day,"Back Day" to R.drawable.back_day,"Legs Day" to R.drawable.leg_day,"Shoulder Day" to R.drawable.shoulder_day,"Arms Day" to R.drawable.arms_day)),
    ("Arnold Split" to listOf("Chest & Back Day" to R.drawable.chest_back_day, "Arms & Shoulders Day" to R.drawable.arms_shoulder_day, "Legs Day" to R.drawable.leg_day))
)

val splitDayToWeekDays = listOf(
    // Push, Pull, Legs Split
    Triple("Push,Pull,Legs Split", "Push Day", listOf("Monday", "Thursday")),
    Triple("Push,Pull,Legs Split", "Pull Day", listOf("Tuesday", "Friday")),
    Triple("Push,Pull,Legs Split", "Legs Day", listOf("Wednesday", "Saturday")),

    // Full Body Split
    Triple("Full Body Split", "Full Body Day", listOf("Monday", "Wednesday", "Friday")),

    // Upper, Lower Body Split
    Triple("Upper, Lower Body Split", "Upper Body Day", listOf("Monday", "Thursday")),
    Triple("Upper, Lower Body Split", "Lower Body Day", listOf("Tuesday", "Friday")),

    // Bro Split
    Triple("Bro Split", "Chest Day", listOf("Monday")),
    Triple("Bro Split", "Back Day", listOf("Tuesday")),
    Triple("Bro Split", "Legs Day", listOf("Wednesday")),
    Triple("Bro Split", "Shoulder Day", listOf("Thursday")),
    Triple("Bro Split", "Arms Day", listOf("Friday")),

    // Arnold Split
    Triple("Arnold Split", "Chest & Back Day", listOf("Monday", "Thursday")),
    Triple("Arnold Split", "Arms & Shoulders Day", listOf("Tuesday", "Friday")),
    Triple("Arnold Split", "Legs Day", listOf("Wednesday", "Saturday")),
)


val weekDays = listOf(
    1 to "Monday",
    2 to "Tuesday",
    3 to "Wednesday",
    4 to "Thursday",
    5 to "Friday",
    6 to "Saturday",
    7 to "Sunday"
)
