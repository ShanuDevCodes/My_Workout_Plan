package com.example.myworkoutplan.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val dayTitle: String,
    val workoutList: List<Pair<String, Int>>
)
@Serializable
object Plans
@Serializable
object Home
@Serializable
object Settings