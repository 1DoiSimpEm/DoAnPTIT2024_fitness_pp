package ptit.vietpq.fitnessapp.domain.model

import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import java.time.LocalDate

data class WorkoutPlan(
    val name: String,
    val scheduledDate: LocalDate,
    val exercises: List<TrainingProgramExerciseResponse>
)
