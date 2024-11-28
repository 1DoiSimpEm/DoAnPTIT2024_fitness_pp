package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkoutPlanResponse(
    @Json(name = "name") val name: String = "",
    @Json(name = "scheduled_date") val scheduledDate: String = "",
    @Json(name = "user_id") val userId: Int = 0 ,
    @Json(name = "training_program_exercise_id") val trainingProgramExerciseId: Int = 0,
    @Json(name = "id") val id: Int = 0 ,
    @Json(name = "training_program_exercise") val trainingProgramExercise: TrainingProgramExerciseResponse
)
