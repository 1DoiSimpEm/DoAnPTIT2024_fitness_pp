package ptit.vietpq.fitnessapp.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProgressRequest(
    @Json(name = "user_id") val userId: Int = 0,
    @Json(name = "training_program_id") val trainingProgramId: Int? = null,
    @Json(name = "exercise_id") val exerciseId: Int? = null,
    @Json(name = "training_program_exercise_id") val trainingProgramExerciseId: Int? = null,
    @Json(name = "completion_date") val completionDate: String = "",
    @Json(name = "sets_completed") val setsCompleted: Int = 0,
    @Json(name = "reps_completed") val repsCompleted: Int = 0,
    @Json(name = "weight_used") val weightUsed: Int = 0,
    @Json(name = "duration") val duration: Int = 0,
    @Json(name = "status") val status: String = "",
    @Json(name = "notes") val notes: String = ""
)