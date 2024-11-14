package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class TrainingProgramExerciseResponse(
    @Json(name = "training_program_id") val trainingProgramId: Int,
    @Json(name = "exercise_id") val exerciseId: Int,
    @Json(name = "sets") val sets: Int,
    @Json(name = "reps") val reps: Int,
    @Json(name = "duration") val duration: Int,
    @Json(name = "rest_time") val restTime: Int,
    @Json(name = "order") val order: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "exercise") val exercise: ExerciseResponse
)

fun TrainingProgramExerciseResponse.toModel() = TrainingProgramExercise(
    trainingProgramId = trainingProgramId,
    exerciseId = exerciseId,
    sets = sets,
    reps = reps,
    duration = duration,
    restTime = restTime,
    order = order,
    id = id,
    exerciseName = exercise.name,
    exerciseDescription = exercise.description,
    exerciseVideoUrl = exercise.videoUrl,
    exerciseImage = exercise.image,
    exerciseMuscleGroupId = exercise.muscleGroupId
)

@Serializable
data class TrainingProgramExercise(
    val trainingProgramId: Int,
    val exerciseId: Int,
    val sets: Int,
    val reps: Int,
    val duration: Int,
    val restTime: Int,
    val order: Int,
    val id: Int,
    val exerciseName: String,
    val exerciseDescription: String,
    val exerciseVideoUrl: String,
    val exerciseImage: String,
    val exerciseMuscleGroupId: Int
)