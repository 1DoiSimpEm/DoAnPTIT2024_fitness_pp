package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProgressResponse(
    @Json(name = "user_id") val userId: Int,
    @Json(name = "training_program_id") val trainingProgramId: Int? = null,
    @Json(name = "exercise_id") val exerciseId: Int? = null,
    @Json(name = "training_program_exercise_id") val trainingProgramExerciseId: Int? = null,
    @Json(name = "completion_date") val completionDate: String,
    @Json(name = "sets_completed") val setsCompleted: Int,
    @Json(name = "reps_completed") val repsCompleted: Int,
    @Json(name = "weight_used") val weightUsed: Int,
    @Json(name = "duration") val duration: Int,
    @Json(name = "status") val status: String,
    @Json(name = "notes") val notes: String,
    @Json(name = "id") val id: Int,
    @Json(name = "training_program") val trainingProgram: TrainingProgramResponse? = null,
    @Json(name = "exercise") val exercise: ExerciseResponse? = null,
    @Json(name = "training_program_exercise") val trainingProgramExercise: TrainingProgramExerciseResponse? = null
)