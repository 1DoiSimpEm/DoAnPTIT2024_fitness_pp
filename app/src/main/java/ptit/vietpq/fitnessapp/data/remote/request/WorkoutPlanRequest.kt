package ptit.vietpq.fitnessapp.data.remote.request

import com.squareup.moshi.Json

data class WorkoutPlanRequest(
    @Json(name = "name") val name: String,
    @Json(name = "scheduled_date") val scheduledDate: String,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "training_program_exercise_id") val trainingProgramExerciseId: Int
)