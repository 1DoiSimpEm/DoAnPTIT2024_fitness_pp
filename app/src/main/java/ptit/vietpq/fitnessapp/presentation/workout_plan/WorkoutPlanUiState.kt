package ptit.vietpq.fitnessapp.presentation.workout_plan

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ptit.vietpq.fitnessapp.domain.model.WorkoutPlan

data class WorkoutPlanUiState(
    val isLoading: Boolean = false,
    val workoutPlans: ImmutableList<WorkoutPlan> = persistentListOf(),
    val error: String? = null
)