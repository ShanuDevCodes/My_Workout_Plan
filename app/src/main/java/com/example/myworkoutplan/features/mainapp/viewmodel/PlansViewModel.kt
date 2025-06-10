package com.example.myworkoutplan.features.mainapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.data.local.workout.WorkoutSplit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlansViewModel : ViewModel() {

    private val _visibleSplits = MutableStateFlow<Set<Int>>(emptySet())
    val visibleSplits: StateFlow<Set<Int>> = _visibleSplits

    private var hasAnimatedSplits = false

    fun animateSplitsOnce(splits: List<WorkoutSplit>) {
        if (hasAnimatedSplits) return
        hasAnimatedSplits = true
        viewModelScope.launch {
            _visibleSplits.value = emptySet()
            splits.forEach { split ->
                delay(100L)
                _visibleSplits.value = _visibleSplits.value + split.id
            }
        }
    }
}