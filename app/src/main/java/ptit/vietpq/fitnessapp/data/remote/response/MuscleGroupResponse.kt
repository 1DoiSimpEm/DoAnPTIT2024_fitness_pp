package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MuscleGroupResponse(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String ="",
    @Json(name = "description") val description: String ="",
    @Json(name = "image") val image: String = "",
)