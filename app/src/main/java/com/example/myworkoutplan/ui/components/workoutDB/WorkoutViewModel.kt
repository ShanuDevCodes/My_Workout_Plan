package com.example.myworkoutplan.ui.components.workoutDB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutViewModel(
    private val dao: WorkoutDao
) : ViewModel(){
    private val _state = MutableStateFlow(WorkoutState())
    private val _currentWorkoutType = MutableStateFlow("")

    private val _workoutTypes = dao.getAllWorkoutTypes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _workoutsByType = _currentWorkoutType
        .flatMapLatest { workoutType ->
            if (workoutType.isNotEmpty()) {
                dao.getWorkoutsByType(workoutType)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(
        _state,
        _workoutTypes,
        _workoutsByType
    ) { state, workoutTypes, workoutsByType ->
        state.copy(
            workoutTypeWithImage = workoutTypes,
            workoutWithImage = workoutsByType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WorkoutState())
    fun onEvent(event: WorkoutEvent){
        when(event){
            is WorkoutEvent.DeleteWorkout -> {
                viewModelScope.launch {
                    dao.deleteWorkout(event.workout)
                }
            }
            WorkoutEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingWorkout = false
                ) }
            }
            WorkoutEvent.SaveWorkout -> {
                val exerciseName = _state.value.exerciseName
                val imageResource = _state.value.imageResource
                val workoutType = _state.value.workoutType
                val workoutTypeImage = _state.value.workoutTypeImage

                if(exerciseName.isBlank() || workoutType.isBlank()) {
                    return
                }

                val workout = WorkoutPlan(
                    exerciseName = exerciseName,
                    imageResource = imageResource,
                    workoutType = workoutType,
                    workoutTypeImage = workoutTypeImage
                )

                viewModelScope.launch {
                    dao.upsertWorkout(workout)
                }
                _state.update { it.copy(
                    isAddingWorkout = false,
                    exerciseName = "",
                    imageResource = 0,
                    workoutType = "",
                    workoutTypeImage = 0
                ) }
            }
            is WorkoutEvent.SetExerciseName -> {
                _state.update { it.copy(
                    exerciseName = event.exerciseName
                ) }
            }
            is WorkoutEvent.SetImageResource -> _state.update { it.copy(
                imageResource = event.imageResource
            ) }
            is WorkoutEvent.SetWorkoutType -> _state.update { it.copy(
                workoutType = event.workoutType
            ) }
            is WorkoutEvent.SetWorkoutTypeImage -> _state.update { it.copy(
                workoutTypeImage = event.workoutTypeImage
            ) }
            WorkoutEvent.ShowDialog -> _state.update { it.copy(
                isAddingWorkout = true
            ) }
            is WorkoutEvent.ShowWorkoutByType -> {
                _currentWorkoutType.value = event.workoutType
            }
            WorkoutEvent.ShowWorkoutTypes -> {
                _currentWorkoutType.value = ""
            }
        }
    }
    fun getExerciseNameAndImagePairsByType(type: String): Flow<List<Pair<String, Int>>> {
        return dao.getWorkoutsByType(type)
            .map { list ->
                list.map { it.workoutName to it.imageResource }
            }
    }
}