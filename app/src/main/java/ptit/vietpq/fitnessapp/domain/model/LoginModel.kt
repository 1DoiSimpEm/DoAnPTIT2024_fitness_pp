package ptit.vietpq.fitnessapp.domain.model

data class LoginModel(
    val status : Int,
    val message: String,
    val tokenType: String,
    val accessToken: String,
    val userName: String,
)