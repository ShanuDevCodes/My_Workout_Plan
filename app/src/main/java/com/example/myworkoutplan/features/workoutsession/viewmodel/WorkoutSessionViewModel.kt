package com.example.myworkoutplan.features.workoutsession.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.data.local.workout.WorkoutPlan
import com.example.myworkoutplan.features.workoutsession.model.WorkoutSessionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WorkoutSetLog(
    val setNumber: Int,
    val weight: String,
    val reps: String,
    val isBodyWeight: Boolean
)

class WorkoutSessionViewModel:ViewModel() {

    private var sessionStarted = false

    private var allExercises: List<WorkoutPlan> = emptyList()


    // StateFlows for UI observation
    private val _currentWorkout = MutableStateFlow<WorkoutPlan?>(null)
    val currentWorkout: StateFlow<WorkoutPlan?> = _currentWorkout

    private val _upcomingWorkouts = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val upcomingWorkouts: StateFlow<List<WorkoutPlan>> = _upcomingWorkouts

    private val _completedWorkouts = MutableStateFlow<List<Pair<WorkoutPlan,Int>>>(emptyList())
    val completedWorkouts: StateFlow<List<Pair<WorkoutPlan,Int>>> = _completedWorkouts

    private val count: MutableStateFlow<Int> = MutableStateFlow(0)
    val countState: StateFlow<Int> = count

    private val countLimit: MutableStateFlow<Int> = MutableStateFlow(3)
    val countLimitState: StateFlow<Int> = countLimit

//    private var isRunning: MutableStateFlow<Boolean> = MutableStateFlow(true)

//    private var timeInMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val timeInMillisState: StateFlow<Long> = WorkoutSessionRepository.timeInMillis

    private var restTimeInMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val restTimeInMillisState: StateFlow<Long> = restTimeInMillis

    private val isResting: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRestingState: StateFlow<Boolean> = isResting

    private var shouldCompleteAfterRest = false

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted

    private val _exitDialog = MutableStateFlow(false)
    val exitDialog: StateFlow<Boolean> = _exitDialog

    private val _skippedWorkoutDialog = MutableStateFlow(false)
    val skippedWorkoutDialog: StateFlow<Boolean> = _skippedWorkoutDialog

    private val _workoutLog = MutableStateFlow<Map<WorkoutPlan, List<WorkoutSetLog>>>(emptyMap())
    val workoutLog: StateFlow<Map<WorkoutPlan, List<WorkoutSetLog>>> = _workoutLog

    private val _workoutSplit = MutableStateFlow("")
    var workoutSplit: StateFlow<String> = _workoutSplit

    private val _workoutDay = MutableStateFlow("")
    val workoutDay: StateFlow<String> = _workoutDay

    fun showExitDialog() {
        _exitDialog.value = true
    }

    fun dismissExitDialog() {
        _exitDialog.value = false
    }

    fun showSkippedWorkoutDialog() {
        _skippedWorkoutDialog.value = true
    }

    fun hideSkippedWorkoutDialog() {
        _skippedWorkoutDialog.value = false
    }

    init {
        viewModelScope.launch {
            if (WorkoutSessionRepository.startTimeInMillis.value == 0L) {
                WorkoutSessionRepository.startTimeInMillis.value = System.currentTimeMillis()
            }
            while (true) {
                if (WorkoutSessionRepository.isRunning.value) {
                    val elapsed = System.currentTimeMillis() - WorkoutSessionRepository.startTimeInMillis.value
                    WorkoutSessionRepository.timeInMillis.value = elapsed
                }
                delay(10L)
            }
        }
        viewModelScope.launch {
            while (true) {
                if (isResting.value) {
                    restTimeInMillis.value -= 10
                    if (restTimeInMillis.value <= 0) {
                        isResting.value = false
                        restTimeInMillis.value = 0
                        if (count.value >= countLimit.value) {
                            completeCurrentWorkout()
                            count.value = 0
                        }
                    }
                }
                delay(10L)
            }
        }
    }

    // Initialize the session with a new exercise list
    fun startSession(workoutPlan: List<WorkoutPlan>,workoutSplit:String,workoutDay:String) {
        if (sessionStarted) return
        sessionStarted = true
        _workoutSplit.value = workoutSplit
        _workoutDay.value = workoutDay
        allExercises = workoutPlan.map { it }
        _completedWorkouts.value = emptyList()
        if (workoutPlan.isNotEmpty()) {
            _currentWorkout.value = workoutPlan.map { it }.first()
            _upcomingWorkouts.value = workoutPlan.map { it }.drop(1)
        } else {
            _currentWorkout.value = null
            _upcomingWorkouts.value = emptyList()
        }
    }

    // Call this when the user completes the current Workout
    private fun completeCurrentWorkout() {
        countLimit.value = 3
        val current = _currentWorkout.value ?: return
        val updatedList = _completedWorkouts.value.toMutableList()
        updatedList.add(current to count.value)
        _completedWorkouts.value = updatedList

        if (_upcomingWorkouts.value.isNotEmpty()) {
            _currentWorkout.value = _upcomingWorkouts.value.first()
            _upcomingWorkouts.value = _upcomingWorkouts.value.drop(1)
        } else {
            _currentWorkout.value = null
            WorkoutSessionRepository.isRunning.value = false
            _isCompleted.value = true
            count.value = 0
        }
    }

    fun workoutSetCompleted() {
        count.value++
        if (count.value < countLimit.value) {
            restTimeInMillis.value = 180000
            isResting.value = true
            shouldCompleteAfterRest = false
        } else {
            restTimeInMillis.value = 300000
            isResting.value = true
        }
    }


    fun skipWorkout(){
        if (count.value > 0){
            val current = _currentWorkout.value?: return
            val updatedList = _completedWorkouts.value.toMutableList()
            updatedList.add(current to count.value)
            _completedWorkouts.value = updatedList
        }
        count.value = 0
        isResting.value = false
        restTimeInMillis.value = 0
        countLimit.value = 3
        if (_upcomingWorkouts.value.isNotEmpty()) {
            _currentWorkout.value = _upcomingWorkouts.value.first()
            _upcomingWorkouts.value = _upcomingWorkouts.value.drop(1)
        } else {
            _currentWorkout.value = null
            WorkoutSessionRepository.isRunning.value = false
            _isCompleted.value = true
            count.value = 0
        }
    }

    fun skipRest() {
        isResting.value = false
        restTimeInMillis.value = 0
        if (count.value >= countLimit.value) {
            completeCurrentWorkout()
            count.value = 0
        }
    }

    fun countLimitIncrease(){
        countLimit.value++
    }

    fun updateSetLog(workout: WorkoutPlan, setLog: WorkoutSetLog) {
        val currentLogs = _workoutLog.value.toMutableMap()
        val logsForWorkout = currentLogs[workout]?.toMutableList() ?: mutableListOf()
        // Replace or add the set log for the setNumber
        val existingIndex = logsForWorkout.indexOfFirst { it.setNumber == setLog.setNumber }
        if (existingIndex >= 0) {
            logsForWorkout[existingIndex] = setLog
        } else {
            logsForWorkout.add(setLog)
        }
        currentLogs[workout] = logsForWorkout
        _workoutLog.value = currentLogs
    }

}