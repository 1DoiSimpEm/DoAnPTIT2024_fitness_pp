package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.data.remote.request.WorkoutPlanRequest
import ptit.vietpq.fitnessapp.data.remote.service.WorkoutPlanService
import ptit.vietpq.fitnessapp.domain.model.WorkoutPlan
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreateWorkoutPlanUseCase @Inject constructor(
    private val workoutPlanService: WorkoutPlanService,
    private val dispatchers: AppCoroutineDispatchers,
    private val sharePreferenceProvider: SharePreferenceProvider,
) {

    suspend operator fun invoke(
        name: String,
        scheduledDate: String,
        trainingProgramExerciseId: Int
    ) = withContext(dispatchers.io) {
        safeApiCall {
            workoutPlanService.createWorkoutPlan(
                WorkoutPlanRequest(
                    name = name,
                    scheduledDate = scheduledDate,
                    userId = sharePreferenceProvider.userId,
                    trainingProgramExerciseId = trainingProgramExerciseId,
                )
            )
        }
    }

    suspend fun createMultipleWorkoutPlans(workoutPlans: WorkoutPlan) =
        withContext(dispatchers.io) {
            workoutPlans.exercises.map { exercise ->
                async {
                        workoutPlanService.createWorkoutPlan(
                            WorkoutPlanRequest(
                                name = workoutPlans.name,
                                scheduledDate = workoutPlans.scheduledDate.format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                ),
                                userId = sharePreferenceProvider.userId,
                                trainingProgramExerciseId = exercise.id
                            )
                        )
                    }
            }.awaitAll()
        }

}