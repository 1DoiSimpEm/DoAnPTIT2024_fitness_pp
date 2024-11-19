package ptit.vietpq.fitnessapp.data.remote.service

import ptit.vietpq.fitnessapp.data.remote.request.WorkoutPlanRequest
import ptit.vietpq.fitnessapp.data.remote.response.WorkoutPlanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkoutPlanService {

    @GET("api/workout-plan/user/{userId}")
    suspend fun getWorkoutPlansByUserAndDate(
        @Path("userId") userId: Int,
        @Query("date") date: String
    ): Response<List<WorkoutPlanResponse>>

    @POST("api/workout-plan")
    suspend fun createWorkoutPlan(
        @Body request: WorkoutPlanRequest
    ): Response<WorkoutPlanResponse>

}