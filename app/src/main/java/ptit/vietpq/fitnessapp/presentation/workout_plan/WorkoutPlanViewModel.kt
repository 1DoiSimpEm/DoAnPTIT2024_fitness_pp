package ptit.vietpq.fitnessapp.presentation.workout_plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.domain.model.WorkoutPlan
import ptit.vietpq.fitnessapp.domain.usecase.CreateWorkoutPlanUseCase
import ptit.vietpq.fitnessapp.domain.usecase.GetTrainingProgramExercisesUseCase
import ptit.vietpq.fitnessapp.domain.usecase.GetWorkoutPlanByDateUseCase
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WorkoutPlanViewModel @Inject constructor(
    private val getWorkoutPlanByDateUseCase: GetWorkoutPlanByDateUseCase,
    private val getTrainingProgramExercisesUseCase: GetTrainingProgramExercisesUseCase,
    private val createWorkoutPlanUseCase: CreateWorkoutPlanUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutPlanUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTrainingProgramExercises()
    }

    fun getWorkoutPlanByDate(date: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            getWorkoutPlanByDateUseCase(date).fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            workoutPlans = result.toImmutableList(),
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    fun getTrainingProgramExercises() {
        viewModelScope.launch {
            getTrainingProgramExercisesUseCase().fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            trainingExercises = result.toImmutableList(),
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    fun createWorkoutPlans(workoutPlans: WorkoutPlan) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                createWorkoutPlanUseCase.createMultipleWorkoutPlans(workoutPlans)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
                getWorkoutPlanByDate(
                    workoutPlans.scheduledDate.format(
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                    )
                )
            } catch (e: Exception) {
                // Handle error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error creating workout plans"
                    )
                }
            }
        }
    }
}