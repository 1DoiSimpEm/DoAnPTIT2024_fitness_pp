package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.data.remote.service.WorkoutPlanService
import javax.inject.Inject

class GetWorkoutPlanByDateUseCase @Inject constructor(
    private val workoutPlanService: WorkoutPlanService,
    private val sharePreferenceProvider: SharePreferenceProvider,
    private val dispatchers: AppCoroutineDispatchers,
) {

    suspend operator fun invoke(
        date: String
    ) = withContext(dispatchers.io) {
        safeApiCall {
            val userId = sharePreferenceProvider.userId
            workoutPlanService.getWorkoutPlansByUserAndDate(userId, date)
        }
    }

}