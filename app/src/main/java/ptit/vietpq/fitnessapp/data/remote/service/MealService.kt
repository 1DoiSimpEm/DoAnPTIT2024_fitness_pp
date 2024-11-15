package ptit.vietpq.fitnessapp.data.remote.service

import ptit.vietpq.fitnessapp.data.remote.request.MealPlanRequest
import ptit.vietpq.fitnessapp.data.remote.response.MealPlanResponse
import ptit.vietpq.fitnessapp.data.remote.response.MealPlanningResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MealService {

    @POST("api/meal-plan/planning")
    suspend fun createMealPlan(@Body mealPlanRequest: MealPlanRequest): Response<MealPlanningResponse>

    @GET("api/meal-plan/meal-plans/user/{userId}")
    suspend fun getMealPlansByUser(@Path("userId") userId: Int): Response<List<MealPlanResponse>>

}