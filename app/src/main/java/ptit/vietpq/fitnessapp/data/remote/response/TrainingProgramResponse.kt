package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingProgramResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "category_id") val categoryId: Int,
    @Json(name = "difficulty_level") val difficultyLevel: String,
    @Json(name = "duration_weeks") val durationWeeks: Int,
    @Json(name = "image_url") val imageUrl: String
)