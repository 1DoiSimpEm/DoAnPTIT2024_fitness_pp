package ptit.vietpq.fitnessapp.data.remote.service

import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExerciseService {
    @GET("api/exercise/exercises")
    suspend fun getExercises(): Response<List<ExerciseResponse>>
}