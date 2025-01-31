package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.remote.service.TrainingService
import javax.inject.Inject

class GetTrainingExerciseByTrainingProgramIDUseCase @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val trainingService: TrainingService,
) {

    suspend operator fun invoke(trainingProgramId: Int) = withContext(dispatchers.io){
        safeApiCall {
            trainingService.getTrainingProgramExercisesByProgram(trainingProgramId)
        }
    }

}