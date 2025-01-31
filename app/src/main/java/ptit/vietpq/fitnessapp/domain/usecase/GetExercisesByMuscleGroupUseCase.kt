package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.remote.service.ExerciseService
import javax.inject.Inject

class GetExercisesByMuscleGroupUseCase @Inject constructor(
    private val exerciseService: ExerciseService,
    private val dispatchers: AppCoroutineDispatchers,
){
    suspend operator fun invoke(muscleGroupID : Int) = withContext(dispatchers.io){
        safeApiCall {
            exerciseService.getExercisesByMuscleGroup(muscleGroupID)
        }
    }
}