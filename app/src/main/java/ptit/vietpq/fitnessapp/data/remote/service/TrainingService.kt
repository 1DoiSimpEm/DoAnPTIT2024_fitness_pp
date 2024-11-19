package ptit.vietpq.fitnessapp.data.remote.service

import ptit.vietpq.fitnessapp.data.remote.response.TrainingCategoryResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrainingService {
    @GET("api/training-program/")
    suspend fun getTrainingPrograms(): Response<List<TrainingProgramResponse>>

    @GET("api/training-category/categories")
    suspend fun getTrainingCategories(): Response<List<TrainingCategoryResponse>>

    @GET("api/training-program/category/{id}")
    suspend fun getTrainingProgramsByCategory(
        @Path("id") categoryId: Int
    ): Response<List<TrainingProgramResponse>>

    @GET("api/training-program-exercises/program/{id}")
    suspend fun getTrainingProgramExercisesByProgram(
        @Path("id") trainingProgramId: Int
    ): Response<List<TrainingProgramExerciseResponse>>

    @GET("/api/training-program-exercises")
    suspend fun getTrainingProgramExercises(): Response<List<TrainingProgramExerciseResponse>>
}