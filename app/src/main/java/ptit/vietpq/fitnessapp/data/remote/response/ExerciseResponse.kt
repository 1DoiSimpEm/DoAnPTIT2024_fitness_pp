package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExerciseResponse(
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "video_url") val videoUrl: String,
    @Json(name = "image") val image: String,
    @Json(name = "muscle_group_id") val muscleGroupId: Int,
    @Json(name = "id") val id: Int
)