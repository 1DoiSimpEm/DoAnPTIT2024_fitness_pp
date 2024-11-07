package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealPlanningResponse(
    @Json(name = "code") val code: Int,
    @Json(name = "message") val message: String,
    @Json(name = "data") val data: Data
)

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "id") val id: Int,
    @Json(name = "description") val description: String,
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "name") val name: String,
    @Json(name = "user_id") val userId: Int
)