package com.example.myworkoutplan.data.local.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.R
import com.example.myworkoutplan.features.mainapp.data.allMuscleGroups
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
) : ViewModel() {
    private val _state = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState> = _state
    fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.DeleteWorkoutByName -> {
                viewModelScope.launch {
                    dao.deleteByExerciseName(event.workoutName)
                }
            }

            WorkoutEvent.HideDialog -> {
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

            WorkoutEvent.SaveWorkout -> {
                val exerciseName = _state.value.exerciseName
                val imageResource = R.drawable.weights
                val workoutType = _state.value.workoutType
                val workoutTypeImage = _state.value.workoutTypeImage

                if (exerciseName.isBlank() || workoutType.isBlank()) {
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
                _state.update {
                    it.copy(
                        exerciseName = event.exerciseName
                    )
                }
            }

            is WorkoutEvent.SetImageResource -> _state.update {
                it.copy(
                    imageResource = event.imageResource
                )
            }

            is WorkoutEvent.SetWorkoutType -> _state.update {
                it.copy(
                    workoutType = event.workoutType
                )
            }

            is WorkoutEvent.SetWorkoutTypeImage -> _state.update {
                it.copy(
                    workoutTypeImage = event.workoutTypeImage
                )
            }

            WorkoutEvent.ShowDialog -> _state.update {
                it.copy(
                    isAddingWorkout = true
                )
            }

            WorkoutEvent.ResetWorkoutDB -> {
                viewModelScope.launch {
                    initialiseDB()
                }
            }


            is WorkoutEvent.DeleteWorkout -> {
                viewModelScope.launch {
                    dao.deleteWorkout(event.workoutPlan)
                }
            }

            is WorkoutEvent.GetWorkoutsByMuscleGroup -> {
                viewModelScope.launch {
                    dao.getWorkoutsByMuscleGroup(event.muscleGroups).collect { workoutList ->
                        _state.update { it.copy(workoutWithMuscleGroups = workoutList) }
                    }
                }
            }

            is WorkoutEvent.DeleteWorkoutByMuscleGroup -> {
                viewModelScope.launch {
                    dao.deleteWorkoutById(event.workoutWithMuscles.workoutPlan.id)
                }
            }
        }
    }

    suspend fun initialiseDB() {
        // Clear old data
        dao.deleteAllWorkouts()
        dao.deleteAllMuscleGroups()

        // Insert all muscle groups first and keep a map of name -> id
        val muscleGroupIdMap = mutableMapOf<String, Long>()
        for (muscleName in allMuscleGroups) {
            val id = dao.upsertMuscleGroup(MuscleGroup(muscleName = muscleName))
            muscleGroupIdMap[muscleName] = id
        }

        // Combine all workouts from push, pull, leg lists
        val allWorkouts = (pushWorkout + pullWorkout + legWorkout).map { (name, image, muscles) ->
            WorkoutPlan(
                exerciseName = name,
                imageResource = image,
            ) to muscles
        }

        // Insert workouts and their muscle group cross references
        for ((workoutPlan, muscleGroups) in allWorkouts) {
            val workoutId = dao.upsertWorkout(workoutPlan)

            for (muscleName in muscleGroups) {
                val muscleId = muscleGroupIdMap[muscleName]
                    ?: throw IllegalStateException("Muscle group '$muscleName' not found in DB!")

                dao.upsertWorkoutMuscleCrossRef(
                    WorkoutMuscleCrossRef(
                        workoutPlanId = workoutId.toInt(),
                        muscleId = muscleId.toInt()
                    )
                )
            }
        }
    }

}