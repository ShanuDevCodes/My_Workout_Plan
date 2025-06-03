package com.example.myworkoutplan.features.mainapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myworkoutplan.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlansViewModel : ViewModel() {
    private val _plans = MutableStateFlow(
        listOf(
            "Push Day" to R.drawable.push_day,
            "Pull Day" to R.drawable.pull_day,
            "Leg Day" to R.drawable.leg_day,
        )
    )
    val plans: StateFlow<List<Pair<String, Int>>> = _plans
}