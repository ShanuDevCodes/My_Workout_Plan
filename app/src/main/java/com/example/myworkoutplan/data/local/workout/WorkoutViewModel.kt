package com.example.myworkoutplan.data.local.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.R
import com.example.myworkoutplan.features.mainapp.data.allMuscleGroups
import com.example.myworkoutplan.features.mainapp.data.allWorkout
import com.example.myworkoutplan.features.mainapp.data.allWorkoutSplit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
                    // Check if Workout already exists
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

            is WorkoutEvent.SetImageResource -> {
                _state.update {
                    it.copy(
                        imageResource = event.imageResource
                    )
                }
            }

            is WorkoutEvent.SetWorkoutType -> {
                _state.update {
                    it.copy(
                        workoutType = event.workoutType
                    )
                }
            }

            is WorkoutEvent.SetWorkoutTypeImage -> {
                _state.update {
                    it.copy(
                        workoutTypeImage = event.workoutTypeImage
                    )
                }
            }

            WorkoutEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingWorkout = true
                    )
                }
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

            is WorkoutEvent.GetWorkoutBySplitDay -> {
                viewModelScope.launch {
                    dao.getWorkoutsBySplitDay(event.splitDayId).collect {workoutList->
                        _state.update {
                            it.copy(workouts = workoutList)
                        }
                    }
                }
            }

            is WorkoutEvent.GetAllSplitDaysForWorkoutSplit -> {
                viewModelScope.launch {
                    dao.getSplitDaysForSplit(event.splitId).collect {splitDays->
                        _state.update {
                            it.copy(splitDays = splitDays)
                        }
                    }
                }
            }
            WorkoutEvent.GetAllWorkoutSplits -> {
                viewModelScope.launch {
                    dao.getAllWorkoutSplits().collect {splits->
                        _state.update {
                            it.copy(workoutSplits = splits)
                        }
                    }
                }
            }

            WorkoutEvent.GetAllSplitDays -> {
                viewModelScope.launch {
                    dao.getAllSplitDays().collect { splitDays ->
                        _state.update {
                            it.copy(splitDays = splitDays)
                        }
                    }
                }
            }

            is WorkoutEvent.GetSplitDay -> {
                viewModelScope.launch {
                    dao.getSplitDaysBySplitId(event.splitDayId).collect { splitDay ->
                        if (splitDay != null) {
                            _state.update { it.copy(splitDay = splitDay) }
                        } else {
                            // Handle not found: maybe clear state or show a message
                            _state.update { it.copy(splitDay = null) }
                        }
                    }
                }
            }

            WorkoutEvent.GetAllWorkouts -> {
                viewModelScope.launch {
                    dao.getAllWorkouts().collect { workouts ->
                        _state.update {
                            it.copy(workouts = workouts)
                        }
                    }
                }
            }

            is WorkoutEvent.DeleteWorkoutFromSplitDay -> {
                viewModelScope.launch {
                    dao.deleteSplitDayWorkoutCrossRefByWorkoutId(event.splitDayId, event.workoutId)
                }
            }

            is WorkoutEvent.GetSplit -> {
                viewModelScope.launch {
                    dao.getWorkoutSplitsBySplitId(splitId = event.splitId).collect { splitDay ->
                        if (splitDay != null) {
                            _state.update { it.copy(split = splitDay) }
                        } else {
                            // Handle not found: maybe clear state or show a message
                            _state.update { it.copy(split = null) }
                        }
                    }
                }
            }
        }
    }

    suspend fun initialiseDB() {
        // 1. Clear old data
        dao.deleteAllWorkoutSplits()
        dao.deleteAllWorkouts()
        dao.deleteAllMuscleGroups()
        dao.resetAllAutoIncrement()

        // 2. Insert all muscle groups and keep a map of name -> id
        val muscleGroupIdMap = mutableMapOf<String, Long>()
        for (muscleName in allMuscleGroups) {
            val id = dao.upsertMuscleGroup(MuscleGroup(muscleName = muscleName))
            muscleGroupIdMap[muscleName] = id
        }

        // 3. Insert all splits and keep a map of splitName -> id
        val splitIdMap = mutableMapOf<String, Long>()
        for ((splitName, _) in allWorkoutSplit) {
            val id = dao.upsertWorkoutSplit(WorkoutSplit(splitName = splitName))
            splitIdMap[splitName] = id
        }

        // 4. Insert all split days and keep a map of (splitName, splitDayName) -> splitDayId
        val splitDayIdMap = mutableMapOf<Pair<String, String>, Long>()
        for ((splitName, splitDays) in allWorkoutSplit) {
            val splitId = splitIdMap[splitName] ?: error("Split not found: $splitName")
            for ((splitDayName, splitDayImage) in splitDays) {
                val id = dao.upsertSplitDay(SplitDay(splitDayName = splitDayName, splitDayImage = splitDayImage, splitId = splitId.toInt()))
                splitDayIdMap[splitName to splitDayName] = id
            }
        }

        // 5. Insert all workouts and their muscle group cross references
        val workoutPlanIdMap = mutableMapOf<String, Long>()
        for (workout in allWorkout) {
            val workoutPlan = WorkoutPlan(
                exerciseName = workout.name,
                imageResource = workout.image
            )
            val workoutId = dao.upsertWorkout(workoutPlan)
            workoutPlanIdMap[workout.name] = workoutId

            // Insert muscle group cross refs
            for (muscleName in workout.muscleGroups) {
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

        // 6. Insert split day ↔ workout cross refs
        for (workout in allWorkout) {
            val workoutId = workoutPlanIdMap[workout.name] ?: error("Workout not found: ${workout.name}")
            for ((splitName, splitDayName) in workout.workoutSplit) {
                val splitDayId = splitDayIdMap[splitName to splitDayName]
                    ?: throw IllegalStateException("Split day '$splitDayName' for split '$splitName' not found in DB!")
                dao.upsertSplitDayWorkoutCrossRef(
                    SplitDayWorkoutCrossRef(
                        splitDayId = splitDayId.toInt(),
                        workoutPlanId = workoutId.toInt()
                    )
                )
            }
        }
    }
}