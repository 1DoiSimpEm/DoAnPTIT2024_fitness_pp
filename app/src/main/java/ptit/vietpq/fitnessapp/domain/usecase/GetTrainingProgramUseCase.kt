package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.remote.service.TrainingService
import javax.inject.Inject

class GetTrainingProgramUseCase @Inject constructor(
    private val trainingService: TrainingService,
    private val dispatchers: AppCoroutineDispatchers,
) {

    suspend operator fun invoke() = withContext(dispatchers.io){
        safeApiCall {
            trainingService.getTrainingPrograms()
        }
    }

}