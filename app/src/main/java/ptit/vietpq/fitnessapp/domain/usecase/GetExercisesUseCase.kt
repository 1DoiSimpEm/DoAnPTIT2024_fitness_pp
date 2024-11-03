package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.remote.service.ExerciseService
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val exerciseService: ExerciseService,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) {
    suspend operator fun invoke() = withContext(appCoroutineDispatchers.io){
        safeApiCall {
            exerciseService.getExercises()
        }
    }
}