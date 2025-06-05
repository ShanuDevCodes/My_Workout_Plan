package com.example.myworkoutplan.features.workoutsession.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _completedWorkouts = MutableStateFlow<List<String>>(emptyList())
    val completedWorkouts: StateFlow<List<String>> = _completedWorkouts

    private val count: MutableStateFlow<Int> = MutableStateFlow(0)
    val countState: StateFlow<Int> = count

    private var isRunning: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isRunningState: StateFlow<Boolean> = isRunning

    private var timeInMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val timeInMillisState: StateFlow<Long> = timeInMillis

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
    }

    // Initialize the session with a new exercise list
    fun startSession(exerciseNames: List<Pair<String,Int>>) {
        if (sessionStarted) return
        sessionStarted = true
        allExercises = exerciseNames.map { it.first }
        _completedWorkouts.value = emptyList()
        if (exerciseNames.isNotEmpty()) {
            _currentWorkout.value = exerciseNames.map { it.first }.first()
            _upcomingWorkouts.value = exerciseNames.map { it.first }.drop(1)
        } else {
            _currentWorkout.value = ""
            _upcomingWorkouts.value = emptyList()
        }
    }

    // Call this when the user completes the current workout
    private fun completeCurrentWorkout() {
        val current = _currentWorkout.value.ifEmpty { return }
        _completedWorkouts.value += current

        if (_upcomingWorkouts.value.isNotEmpty()) {
            _currentWorkout.value = _upcomingWorkouts.value.first()
            _upcomingWorkouts.value = _upcomingWorkouts.value.drop(1)
        } else {
            _currentWorkout.value = ""
            isRunning.value = false  // Stop timer
            _isCompleted.value = true  // Mark session as completed
        }
    }

    fun workoutSetCompleted(){
        count.value++
        if(count.value == 4){
            completeCurrentWorkout()
            count.value = 0
        }
    }

    fun pauseWorkout() {
        isRunning.value = false
    }

    fun resumeWorkout() {
        isRunning.value = true
    }


    fun skipWorkout(){
        completeCurrentWorkout()
    }
}