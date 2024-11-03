package ptit.vietpq.fitnessapp.data.remote.request

import com.squareup.moshi.Json

data class UserInfoRequest(
    @Json(name = "height") val height: Int,
    @Json(name = "weight") val weight: Int,
    @Json(name = "age") val age: Int,
    @Json(name = "gender") val gender: String?,
    @Json(name = "profile_picture") val profilePicture: String?,
)