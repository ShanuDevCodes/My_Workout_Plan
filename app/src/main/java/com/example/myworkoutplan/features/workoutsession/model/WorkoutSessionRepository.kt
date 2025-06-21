package com.example.myworkoutplan.features.workoutsession.model

import kotlinx.coroutines.flow.MutableStateFlow

object WorkoutSessionRepository {
    val startTimeInMillis = MutableStateFlow(0L)
    val isRunning = MutableStateFlow(true)
    val timeInMillis = MutableStateFlow(0L)
}