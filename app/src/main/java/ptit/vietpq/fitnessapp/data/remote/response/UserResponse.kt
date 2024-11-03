package ptit.vietpq.fitnessapp.data.remote.response
import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "username") val username: String?,
    @Json(name = "email") val email: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "hashed_password") val hashedPassword: String?,
    @Json(name = "full_name") val fullName: String?,
    @Json(name = "height") val height: Int?,
    @Json(name = "weight") val weight: Int?,
    @Json(name = "age") val age: Int?,
    @Json(name = "gender") val gender: String?,
    @Json(name = "profile_picture") val profilePicture: String?,
    @Json(name = "is_admin") val isAdmin: Boolean?
)