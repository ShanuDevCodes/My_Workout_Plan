package com.example.myworkoutplan.data.local.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.R
import com.example.myworkoutplan.features.mainapp.data.legWorkout
import com.example.myworkoutplan.features.mainapp.data.pullWorkout
import com.example.myworkoutplan.features.mainapp.data.pushWorkout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val dao: WorkoutDao
) : ViewModel(){
    private val _state = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState> = _state
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
                    workoutTypeImage = 0,
                    nameAlreadyExists = false
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

                viewModelScope.launch {
                    // Check if workout already exists
                    val existingWorkout = dao.getWorkoutByName(exerciseName)
                    if (existingWorkout != null) {
                        // Update state to indicate the name already exists
                        _state.update { it.copy(nameAlreadyExists = true) }
                        return@launch
                    }

                    val workout = WorkoutPlan(
                        exerciseName = exerciseName,
                        imageResource = imageResource,
                        workoutType = workoutType,
                        workoutTypeImage = workoutTypeImage
                    )
                    dao.upsertWorkout(workout)
                    _state.update {
                        it.copy(
                            isAddingWorkout = false,
                            exerciseName = "",
                            imageResource = 0,
                            workoutType = "",
                            workoutTypeImage = 0,
                            nameAlreadyExists = false
                        )
                    }
                }
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
    suspend fun initialiseDB(){
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