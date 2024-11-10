package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealPlanResponse(
    @Json(name ="id") val id : Int,
    @Json(name ="name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name ="user_id") val userId: Int,
    @Json(name ="time_stamp") val timeStamp: Long,
)