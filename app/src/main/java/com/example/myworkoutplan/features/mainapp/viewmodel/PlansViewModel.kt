package com.example.myworkoutplan.features.mainapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.data.local.workout.WorkoutSplit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlansViewModel : ViewModel() {

    private val _splitVisibilityMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val splitVisibilityMap: StateFlow<Map<Int, Boolean>> = _splitVisibilityMap.asStateFlow()

    private var hasAnimatedSplits = false

    fun animateSplitsOnce(splits: List<WorkoutSplit>) {
        if (hasAnimatedSplits || splits.isEmpty()) return
        hasAnimatedSplits = true

        viewModelScope.launch {
            // Initialize all splits as invisible
            _splitVisibilityMap.value = splits.associate { it.id to false }

            splits.forEach { split ->
                delay(50L)
                // Update only the specific split
                _splitVisibilityMap.value = _splitVisibilityMap.value.toMutableMap().apply {
                    this[split.id] = true
                }
            }
        }
    }

    fun resetAnimation() {
        hasAnimatedSplits = false
        _splitVisibilityMap.value = emptyMap()
    }
}
