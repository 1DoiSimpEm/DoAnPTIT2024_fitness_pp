package ptit.vietpq.fitnessapp.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealPlanRequest(
    @Json(name = "user_id") val userId: Int,
    @Json(name = "prompt") val prompt: String
)