package com.example.myworkoutplan.ui.components.workoutDB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutViewModel(
    private val dao: WorkoutDao
) : ViewModel(){
    val _state = MutableStateFlow(WorkoutState())
    fun onEvent(event: WorkoutEvent){
        when(event){
            is WorkoutEvent.DeleteWorkout -> {
                viewModelScope.launch {
                    dao.deleteByExerciseName(event.workoutName)
                }
            }
            WorkoutEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingWorkout = false,
                    exerciseName = "",
                    imageResource = 0,
                    workoutType = "",
                    workoutTypeImage = 0
                )
                }
            }
            WorkoutEvent.SaveWorkout -> {
                val exerciseName = _state.value.exerciseName
                val imageResource = R.drawable.weights
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

            WorkoutEvent.ResetWorkoutDB -> {
                viewModelScope.launch {
                    dao.deleteAllWorkouts()
                    val push = pushWorkout.map { (name, image) ->
                        WorkoutPlan(name, image, "Push Day", R.drawable.push_day)
                    }
                    val pull = pullWorkout.map { (name, image) ->
                        WorkoutPlan(name, image, "Pull Day", R.drawable.pull_day)
                    }
                    val leg = legWorkout.map { (name, image) ->
                        WorkoutPlan(name, image, "Leg Day", R.drawable.leg_day)
                    }

                    (push + pull + leg).forEach { dao.upsertWorkout(it) }
                }
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