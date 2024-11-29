package ptit.vietpq.fitnessapp.domain.model

data class ProgressData(
    val setsCompleted: Int = 0,
    val repsCompleted: Int = 0,
    val weightUsed: Int = 0,
    val notes: String = ""
)