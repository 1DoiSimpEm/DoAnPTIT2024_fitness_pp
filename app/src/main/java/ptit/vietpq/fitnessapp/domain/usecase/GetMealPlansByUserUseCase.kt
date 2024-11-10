package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.data.remote.service.MealService
import javax.inject.Inject

class GetMealPlansByUserUseCase @Inject constructor(
    private val mealService: MealService,
    private val dispatchers: AppCoroutineDispatchers,
    private val sharePreferencesProvider: SharePreferenceProvider,
) {

    suspend operator fun invoke() = withContext(dispatchers.io){
        safeApiCall {
            mealService.getMealPlansByUser(sharePreferencesProvider.userId)
        }
    }

}