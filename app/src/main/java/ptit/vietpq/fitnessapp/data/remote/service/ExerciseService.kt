package ptit.vietpq.fitnessapp.data.remote.service

import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.MuscleGroupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseService {
    @GET("api/exercise/exercises")
    suspend fun getExercises(): Response<List<ExerciseResponse>>

    @GET("api/muscle/muscle_groups?skip=0&limit=100")
    suspend fun getMuscleGroups(): Response<List<MuscleGroupResponse>>

    @GET("api/exercise/exercises/muscle_group/{id}")
    suspend fun getExercisesByMuscleGroup(@Path("id") muscleGroupId: Int): Response<List<ExerciseResponse>>
}