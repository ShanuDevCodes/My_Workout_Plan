package com.example.myworkoutplan.features.workoutsession.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.data.local.workout.WorkoutWithMuscles
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutSessionViewModel:ViewModel() {

    private var sessionStarted = false

    private var allExercises: List<String> = emptyList()


    // StateFlows for UI observation
    private val _currentWorkout = MutableStateFlow<String>("")
    val currentWorkout: StateFlow<String> = _currentWorkout

    private val _upcomingWorkouts = MutableStateFlow<List<String>>(emptyList())
    val upcomingWorkouts: StateFlow<List<String>> = _upcomingWorkouts

    private val _completedWorkouts = MutableStateFlow<List<Pair<String,Int>>>(emptyList())
    val completedWorkouts: StateFlow<List<Pair<String,Int>>> = _completedWorkouts

    private val count: MutableStateFlow<Int> = MutableStateFlow(0)
    val countState: StateFlow<Int> = count

    private val countLimit: MutableStateFlow<Int> = MutableStateFlow(3)
    val countLimitState: StateFlow<Int> = countLimit

    private var isRunning: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private var timeInMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val timeInMillisState: StateFlow<Long> = timeInMillis

    private var restTimeInMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val restTimeInMillisState: StateFlow<Long> = restTimeInMillis

    private val isResting: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRestingState: StateFlow<Boolean> = isResting

    private var shouldCompleteAfterRest = false

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted

    init {
        viewModelScope.launch {
            while (true) {
                if (isRunning.value) {
                    timeInMillis.value += 10
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
    fun startSession(workoutWithMuscleGroups: List<WorkoutWithMuscles>) {
        if (sessionStarted) return
        sessionStarted = true
        allExercises = workoutWithMuscleGroups.map { it.workoutPlan.exerciseName }
        _completedWorkouts.value = emptyList()
        if (workoutWithMuscleGroups.isNotEmpty()) {
            _currentWorkout.value = workoutWithMuscleGroups.map { it.workoutPlan.exerciseName }.first()
            _upcomingWorkouts.value = workoutWithMuscleGroups.map { it.workoutPlan.exerciseName }.drop(1)
        } else {
            _currentWorkout.value = ""
            _upcomingWorkouts.value = emptyList()
        }
    }

    // Call this when the user completes the current Workout
    private fun completeCurrentWorkout() {
        countLimit.value = 3
        val current = _currentWorkout.value.ifEmpty { return }
        val updatedList = _completedWorkouts.value.toMutableList()
        updatedList.add(current to count.value)
        _completedWorkouts.value = updatedList

        if (_upcomingWorkouts.value.isNotEmpty()) {
            _currentWorkout.value = _upcomingWorkouts.value.first()
            _upcomingWorkouts.value = _upcomingWorkouts.value.drop(1)
        } else {
            _currentWorkout.value = ""
            isRunning.value = false
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
            val current = _currentWorkout.value.ifEmpty { return }
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
            _currentWorkout.value = ""
            isRunning.value = false
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
}