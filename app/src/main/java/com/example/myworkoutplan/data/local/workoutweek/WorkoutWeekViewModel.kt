package com.example.myworkoutplan.data.local.workoutweek

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.core.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class WorkoutWeekViewModel(
    private val dataStoreManager: DataStoreManager,
    private val dao: WorkoutWeekDao
):ViewModel() {
    init {
        checkAndResetWeeklyPlanIfNeeded()
        loadAvailableSwapDays()
    }
    private val _state = MutableStateFlow(WorkoutWeekState())
    val state: StateFlow<WorkoutWeekState> = _state
    private val currentDayOfWeek: Int
        get() = LocalDate.now().dayOfWeek.value

    private fun loadAvailableSwapDays() {
        viewModelScope.launch {
            dao.getAllWorkoutDays().collect { allDays ->
                val tomorrow = LocalDate.now().plusDays(1).dayOfWeek.value
                val filtered = allDays.filter { it.dayOfWeek in tomorrow..7 }
                _state.update { it.copy(availableSwapDays = filtered) }
            }
        }
    }


    fun getDay(){
        viewModelScope.launch {
            val workout = dao.getWorkoutWeek(currentDayOfWeek)
            _state.update {
                it.copy(currentWorkoutDay = null)
            }
            _state.update {
                it.copy(currentWorkoutDay = workout)
            }
        }
    }

    private fun getDefaultWorkoutWeekPlan(): List<WorkoutWeek> = listOf(
        WorkoutWeek(1, "Push Day"),
        WorkoutWeek(2, "Pull Day"),
        WorkoutWeek(3, "Leg Day"),
        WorkoutWeek(4, "Push Day"),
        WorkoutWeek(5, "Pull Day"),
        WorkoutWeek(6, "Leg Day"),
        WorkoutWeek(7, "Rest Day")
    )

    private fun checkAndResetWeeklyPlanIfNeeded() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val lastResetDate = dataStoreManager.getLastResetDate().firstOrNull()

            val currentWeekStart = today.with(DayOfWeek.MONDAY)

            val shouldReset = lastResetDate == null ||
                    lastResetDate.isBefore(currentWeekStart)

            if (shouldReset) {
                dao.deleteAll()
                dao.insertAll(getDefaultWorkoutWeekPlan())
                dataStoreManager.setLastResetDate(today)
            }

            getDay()
        }
    }

    fun onEvent(event: WorkoutWeekEvent) {
        when (event) {
            is WorkoutWeekEvent.SetDayOfWeek -> {
                _state.update {
                    it.copy(
                        dayOfWeek = event.dayOfWeek
                    )
                }
            }

            is WorkoutWeekEvent.SetWorkoutType -> TODO()

            is WorkoutWeekEvent.SwapWorkoutWeek -> {
                viewModelScope.launch {
                    swapWorkouts(event.weekDay, currentDayOfWeek)
                    getDay()
                }
            }

            WorkoutWeekEvent.HideSwapDialog -> {
                _state.update {
                    it.copy(
                        isSwapping = false
                    )
                }
            }
            WorkoutWeekEvent.ShowSwapDialog -> {
                _state.update {
                    it.copy(
                        isSwapping = true
                    )
                }
            }
        }
    }
    private suspend fun swapWorkouts(day1: Int, day2: Int) {
        val workout1 = dao.getWorkoutWeek(day1)
        val workout2 = dao.getWorkoutWeek(day2)

        if (workout1 != null && workout2 != null) {
            dao.update(workout1.copy(workoutType = workout2.workoutType))
            dao.update(workout2.copy(workoutType = workout1.workoutType))
        }
        getDay()
    }
}