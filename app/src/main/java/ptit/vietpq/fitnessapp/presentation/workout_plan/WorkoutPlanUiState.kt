package ptit.vietpq.fitnessapp.presentation.workout_plan

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.WorkoutPlanResponse

data class WorkoutPlanUiState(
    val isLoading: Boolean = false,
    val workoutPlans: ImmutableList<WorkoutPlanResponse> = persistentListOf(),
    val trainingExercises : ImmutableList<TrainingProgramExerciseResponse> = persistentListOf(),
    val error: String? = null
)