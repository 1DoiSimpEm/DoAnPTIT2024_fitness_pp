package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.remote.request.MealPlanRequest
import ptit.vietpq.fitnessapp.data.remote.service.MealService
import javax.inject.Inject

class CreateMealPlanUseCase  @Inject constructor(
    private val mealPlanService: MealService,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) {
    suspend operator fun invoke(mealPlanRequest: MealPlanRequest) = withContext(appCoroutineDispatchers.io){
        safeApiCall {
            mealPlanService.createMealPlan(mealPlanRequest)
        }
    }
}