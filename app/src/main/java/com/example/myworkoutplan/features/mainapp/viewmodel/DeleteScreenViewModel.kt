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
            // PPL Split
            "Push Day" -> listOf("Chest", "Triceps", "Shoulders")
            "Pull Day" -> listOf("Back", "Biceps")
            "Leg Day" -> listOf("Quads", "Glutes", "Hamstrings", "Calves")

            // Full Body
            "Full Body Day" -> listOf(
                "Chest", "Back", "Shoulders", "Biceps", "Triceps", "Quads", "Glutes", "Hamstrings", "Calves", "Core"
            )

            // Upper/Lower Split
            "Upper Body" -> listOf("Chest", "Back", "Shoulders", "Biceps", "Triceps")
            "Lower Body" -> listOf("Quads", "Glutes", "Hamstrings", "Calves")

            // Bro Split
            "Chest Day" -> listOf("Chest")
            "Back Day" -> listOf("Back", "Lats", "Lower Back", "Traps")
            "Shoulders Day" -> listOf("Shoulders", "Traps")
            "Arms Day" -> listOf("Biceps", "Triceps", "Forearms")

            // Arnold Split
            "Chest & Back" -> listOf("Chest", "Back", "Lats", "Traps")
            "Arms & Shoulders" -> listOf("Biceps", "Triceps", "Shoulders", "Forearms")
            "Legs" -> listOf("Quads", "Glutes", "Hamstrings", "Calves")

            else -> emptyList()
        }
}