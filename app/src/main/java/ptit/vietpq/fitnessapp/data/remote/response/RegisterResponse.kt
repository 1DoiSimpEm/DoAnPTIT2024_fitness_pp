package ptit.vietpq.fitnessapp.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @Json(name = "message") val message: String,
    @Json(name = "status_code") val status: Int,
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
)