package ptit.vietpq.fitnessapp.domain.model

import com.squareup.moshi.Json

data class RegisterModel(
    val status : Int,
    val message: String,
    val accessToken: String,
    val tokenType: String,
)