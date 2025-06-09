package com.example.myworkoutplan.features.mainapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myworkoutplan.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlansViewModel : ViewModel() {

    // PPL Split (Push Pull Legs)
    private val _pplPlans = MutableStateFlow(
        listOf(
            "Push Day" to R.drawable.push_day,
            "Pull Day" to R.drawable.pull_day,
            "Leg Day" to R.drawable.leg_day,
        )
    )
    val pplPlans: StateFlow<List<Pair<String, Int>>> = _pplPlans

    // Full Body Split
    private val _fullBodyPlans = MutableStateFlow(
        listOf(
            "Full Body Day" to R.drawable.ai, // replace with appropriate image
        )
    )
    val fullBodyPlans: StateFlow<List<Pair<String, Int>>> = _fullBodyPlans

    // Upper Lower Split
    private val _upperLowerSplit = MutableStateFlow(
        listOf(
            "Upper Body" to R.drawable.ai,  // replace with appropriate image
            "Lower Body" to R.drawable.ai   // replace with appropriate image
        )
    )
    val upperLowerSplit: StateFlow<List<Pair<String, Int>>> = _upperLowerSplit

    // Bro Split
    private val _broSplit = MutableStateFlow(
        listOf(
            "Chest Day" to R.drawable.ai,         // replace with appropriate image
            "Back Day" to R.drawable.ai,           // replace with appropriate image
            "Leg Day" to R.drawable.leg_day,
            "Shoulders Day" to R.drawable.ai, // replace with appropriate image
            "Arms Day" to R.drawable.ai            // replace with appropriate image
        )
    )
    val broSplit: StateFlow<List<Pair<String, Int>>> = _broSplit

    // Arnold Split
    private val _arnoldSplit = MutableStateFlow(
        listOf(
            "Chest & Back" to R.drawable.ai,       // replace with appropriate image
            "Arms & Shoulders" to R.drawable.ai, // replace with appropriate image
            "Legs" to R.drawable.leg_day
        )
    )
    val arnoldSplit: StateFlow<List<Pair<String, Int>>> = _arnoldSplit
}
