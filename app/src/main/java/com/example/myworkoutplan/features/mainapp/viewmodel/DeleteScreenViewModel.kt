package com.example.myworkoutplan.features.mainapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DeleteScreenViewModel: ViewModel() {
    val _dayTitle = mutableStateOf("")

    fun setDayTitle(title: String) {
        _dayTitle.value = title
    }
    val muscleGroups: List<String>
        get() = when (_dayTitle.value) {
            "Push Day" -> listOf("Chest", "Triceps", "Shoulders")
            "Pull Day" -> listOf("Back", "Biceps")
            "Leg Day" -> listOf("Quads", "Glutes", "Hamstrings", "Calves")
            else -> emptyList()
        }
}